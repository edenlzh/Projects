package proj.concert.service.services;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import proj.concert.common.dto.*;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.Booking;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Performer;
import proj.concert.service.domain.Seat;
import proj.concert.service.domain.User;
import proj.concert.service.mapper.BookingMapper;
import proj.concert.service.mapper.ConcertMapper;
import proj.concert.service.mapper.PerformerMapper;
import proj.concert.service.mapper.SeatMapper;
import proj.concert.service.jaxrs.ConcertSubscription;
import proj.concert.service.util.TheatreLayout;

@Resource
@Path("/concert-service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class ConcertResource {
    private static final String AUTH_COOKIE = "auth";
    private static final ConcurrentHashMap<LocalDateTime, LinkedList<ConcertSubscription>> subscriptions = new ConcurrentHashMap<>();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private EntityManager em;

    /**
     * 
     * Retrieves the details of a single concert with the given concertId.
     * 
     * @param concertId The id of the concert to retrieve.
     * @return A {@link ConcertDTO} representing the details of the requested
     *         concert, or
     *         null if no concert exists with the given concertId.
     * @throws NotFoundException if there is no Concert matching the provided ID
     * @author <a href="https://github.com/curdledSoy">Tom Brittenden</a>
     */
    @GET
    @Path("/concerts/{concertId}")

    public ConcertDTO getSingleConcert(@PathParam("concertId") Long concertId) {
        try {

            em = getEM();
            Concert concert = em.find(Concert.class, concertId);
            if (concert == null) {
                throw new NotFoundException();
            }
            ConcertDTO concertDTO = ConcertMapper.toDTO(concert);

            return concertDTO;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    /**
     * Retrieves a list of {@link ConcertDTO} objects
     * 
     * @return List<ConcertDTO> containing all {@link ConcertDTO ConcertDTO's}
     * @author <a href="https://github.com/curdledSoy">Tom Brittenden</a>
     */
    @GET
    @Path("/concerts/")
    public List<ConcertDTO> getAllConcerts() {
        try {
            em = getEM();
            TypedQuery<Concert> query = em.createQuery("SELECT c FROM Concert c", Concert.class);
            List<Concert> concerts = query.getResultList();

            List<ConcertDTO> concertDTOs = ConcertMapper.toDTOList(concerts);

            return concertDTOs;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    /**
     * Retrieves a list of {@link ConcertSummaryDTO} objects
     * 
     * @return List<ConcertSummaryDTO> containing all {@link ConcertSummaryDTO
     *         ConcertSummaryDTO's}
     * 
     * @author @curdledSoy
     */
    @GET
    @Path("/concerts/summaries")
    public List<ConcertSummaryDTO> getAllConcertSummaries() {
        try {
            em = getEM();

            TypedQuery<Concert> query = em.createQuery("SELECT c FROM Concert c", Concert.class);
            List<Concert> concerts = query.getResultList();

            List<ConcertSummaryDTO> concertSummaryDTOs = ConcertMapper.toSummaryDTOList(concerts);

            return concertSummaryDTOs;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    /**
     * Retrieves a list of {@link PerformerDTO} objects
     * 
     * @return List<PerformerDTO> containing all {@link PerformerDTO PerformerDTO's}
     * 
     * @author <a href="https://github.com/curdledSoy">Tom Brittenden</a>
     */
    @GET
    @Path("/performers/")

    public List<PerformerDTO> getAllPerformers() {
        try {
            em = getEM();

            TypedQuery<Performer> query = em.createQuery("SELECT p FROM Performer p", Performer.class);
            List<Performer> performers = query.getResultList();

            List<PerformerDTO> performerDTOs = PerformerMapper.toDTOList(performers);

            return performerDTOs;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    /**
     * Retrieves the details of a single performer with the given performerId.
     * 
     * @param performerId The id of the performer to retrieve.
     * @return A {@link Performer} representing the details of the requested
     *         performer;
     * @throws NotFoundException if there is no Performer matching the provided ID
     * @author <a href="https://github.com/curdledSoy">Tom Brittenden</a>
     * 
     */
    @GET
    @Path("/performers/{performerId}")

    public PerformerDTO getSinglePerformer(@PathParam("performerId") Long performerId) {
        try {
            em = getEM();
            Performer p = em.find(Performer.class, performerId);
            if (p == null) {
                throw new NotFoundException();
            }
            PerformerDTO pDTO = PerformerMapper.toDTO(p);

            return pDTO;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    /**
    /**
     * This method is used to subscribe a user to a concert.
     * 
     * @param res        The AsyncResponse object that will be used to resume the
     *                   response.
     * @param authCookie The authentication cookie of the user.
     * @param subRequest The subscription request containing the concert id and
     *                   percentage booked.
     * @author @tonneaus and <a href="https://github.com/curdledSoy">Tom
     *         Brittenden</a>
     */
    @POST
    @Path("/subscribe/concertInfo")
    public void subscribe(
        @Suspended AsyncResponse res,
        @CookieParam(AUTH_COOKIE) Cookie authCookie,
        ConcertInfoSubscriptionDTO subRequest
    ) {
        // Get the EntityManager and User from the authentication cookie
        EntityManager em = getEM();
        User u = getUserFromCookie(authCookie, em);

        // If the user is not authenticated, return a 401 Unauthorized response
        if (authCookie == null) {
            res.resume(Response.status(Status.UNAUTHORIZED).build());
            return;
        }

        // Try to find the concert with the given ID within subRequest
        Concert concert = em.find(Concert.class, subRequest.getConcertId());
        if (concert == null) {
            // If the concert cannot be found, return a 400 Bad Request response
            res.resume(Response.status(Status.BAD_REQUEST).build());
        }
        if (!concert.getDates().contains(subRequest.getDate())) {
            // If the requested date does not exist for this concert, return Bad Request
            res.resume(Response.status(Status.BAD_REQUEST).build());
        }


        // Synchronize access to the subscriptions list and add the
        // subscription to it
        synchronized (subscriptions) {
            if (!subscriptions.contains(subRequest.getDate())) {
                subscriptions.put(subRequest.getDate(), new LinkedList<>());
            }
        }
        ConcertSubscription sub = new ConcertSubscription(res, subRequest.getPercentageBooked());
        subscriptions.get(subRequest.getDate()).add(sub);

        em.close();
    }

    /**
     * Retrieves seats that will exist on a given date.
     * 
     * @param date   The date on which the seats will exist.
     * @param status The desired booked status of the seats: "Any" / "Booked" /
     *               "Unbooked".
     * @return A {@link Response} either: 200 OK containing {@link SeatDTO}
     *         objects, or 400 Bad Request if the given "stauts" query parameter
     *         is not one of "Any", "Booked", and "Unbooked".
     * @author @tonneaus
     * 
     */
    @GET
    @Path("/seats/{date}")
    public Response getSeats(
        @PathParam("date") String dateString,
        @DefaultValue("Any") @QueryParam("status") BookingStatus status
    ) {
        // parse requested date
        LocalDateTime date = LocalDateTime.parse(dateString);

        // search for seats
        em = getEM();
        em.getTransaction().begin();
        try {
            List<Seat> seats;

            // if we want all bookings, perform this query
            if (status == status.Any) {
                seats = em.createQuery(
                    "SELECT seat FROM Seat seat WHERE date = :date ORDER BY label",
                    Seat.class)
                .setParameter("date", date)
                .getResultList();

            // otherwise perform this query which specifies the `is_booked` column
            } else {
                seats = em.createQuery(
                    "SELECT seat FROM Seat seat WHERE date = :date AND is_booked = :isBooked ORDER BY label",
                    Seat.class)
                .setParameter("date", date)
                .setParameter("isBooked", status == status.Booked)
                .getResultList();
            }

            // make `SeatDTO`s for all resultant seats
            List<SeatDTO> seatDTOs = SeatMapper.toDTOList(seats);
            return Response.ok(seatDTOs).build();

        } finally {
            em.getTransaction().commit();
            em.close();
        }
    }

    /**
     * This method checks the concert subscribers and sends a notification to them
     * if the booked percentage is greater than or equal to their target percentage.
     * 
     * @param datetime         The date and time of the concert.
     * @param seatAvailability The number of seats available for the concert.
     * @author <a href="https://github.com/curdledSoy">Tom Brittenden</a> & @tonneaus
     */
    private void notifySubscribers(LocalDateTime datetime, int seatAvailability) {
        executor.submit(() -> {
            double bookedPercentage = (1.0 - seatAvailability / (double) TheatreLayout.NUM_SEATS_IN_THEATRE) * 100;

            // Iterate through the list of subscriptions
            for (Iterator<ConcertSubscription> iterator = subscriptions.get(datetime).iterator(); iterator.hasNext();) {
                ConcertSubscription sub = iterator.next();

                // Check if the booked percentage is greater than or equal to the target
                // percentage
                if (bookedPercentage >= sub.targetPercentage) {
                    // Remove the subscription so they only receive notification once
                    iterator.remove();

                    // Send the notification
                    ConcertInfoNotificationDTO notification = new ConcertInfoNotificationDTO(seatAvailability);
                    sub.response.resume(Response.ok(notification).build());
                }
            }
        });
    }

    /**
     * Retrieves all bookings for the requesting user or a specific booking for
     * the requesting user.
     * 
     * @param cookie    The user's cookie, used to idenitfy and authenticate the
     *                  requesting user.
     * @param bookingId Optionally specify a specific booking ID to search for.
     * @return A {@link Response}: either 401 Unauthorized if no user is logged
     *         in, or: if `bookingId` is not specified, 200 OK containing
     *         {@link BookingDTO} objects; and if `bookingId` is specified,
     *         either 403 Forbidden if the booking does not exist at all or does
     *         not belong to the requesting user, or 200 OK containing a
     *         {@link BookingDTO} object for the requested booking.
     * @author @tonneaus
     * 
     */
    @GET
    @Path("/bookings")
    public Response getBookings(
        @CookieParam(AUTH_COOKIE) Cookie cookie,
        @QueryParam("bookingId") Long bookingId
    ) {
        EntityManager em = getEM();
        em.getTransaction().begin();

        // check user is logged in
        User user = getUserFromCookie(cookie, em);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // If `bookingId` was not supplied, fill `bookingDTOs` with a `BookingDTO`
        // for each booking by this user. If `bookingId` was supplied, only fill
        // with the booking with the given ID (if it exists).
        List<BookingDTO> bookingDTOs = new ArrayList<BookingDTO>();
        for (Booking booking : user.getBookings()) {
            if (bookingId != null && booking.getId() != bookingId)
                continue;
            bookingDTOs.add(BookingMapper.toDTO(booking));
        }
        em.getTransaction().commit();
        em.close();

        // `bookingId` was not supplied, return all `BookingDTO`s
        if (bookingId == null)
            return Response.ok(bookingDTOs).build();

        // `bookingId` was supplied but the booking was not found, respond with
        // 403 Forbidden
        else if (bookingDTOs.size() == 0)
            return Response.status(Response.Status.FORBIDDEN).build();

        // `bookingId` was supplied and the booking found, return the
        // `BookingDTO` for this booking
        else
            return Response.ok(bookingDTOs.get(0)).build();

    }

    @POST
    @Path("/bookings")
    public Response makeBooking(
        BookingRequestDTO bReq,
        @CookieParam(AUTH_COOKIE) Cookie cookie
    ) {
        long bookingId;
        EntityManager em = getEM();
        try {
            // check user is logged in
            User user = getUserFromCookie(cookie, em);
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            try {
                em.getTransaction().begin();

                // check concert exists
                Concert concert = em.find(Concert.class, bReq.getConcertId());
                if (concert == null) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }

                // check each requested seat
                ArrayList<Seat> seats = new ArrayList<Seat>();
                for (String label : bReq.getSeatLabels()) {
                    Seat seat;
                    try {
                        seat = em.createQuery(
                                "SELECT seat FROM Seat seat WHERE label = :label AND date = :date",
                                Seat.class)
                            .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
                            .setParameter("label", label)
                            .setParameter("date", bReq.getDate())
                            .getSingleResult();
                    } catch (NoResultException e) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                    if (seat.isBooked()) {
                        return Response.status(Response.Status.FORBIDDEN).build();
                    }
                    seats.add(seat);
                }

                // make booking
                for (Seat seat : seats) {
                    seat.setBooked(true);
                    em.persist(seat);
                }
                Booking booking = new Booking(bReq.getConcertId(), bReq.getDate(), seats);
                Set<Booking> bookings = user.getBookings();
                bookings.add(booking);
                user.setBookings(bookings);
                em.persist(user);
                em.persist(booking);
                bookingId = booking.getId();

                int freeSeats = em.createQuery("SELECT COUNT(s) FROM Seat s WHERE s.date = :date AND s.isBooked = false", Long.class)
					.setParameter("date", bReq.getDate())
					.getSingleResult()
					.intValue();
                notifySubscribers(bReq.getDate(), freeSeats);
            } finally {
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }

        // redirect to URI for this booking
        URI uri = URI.create(String.format("/concert-service/bookings?bookingId=%d", bookingId));
        return Response.created(uri).build();
    }

    /**
     * Logs in a user with the given credentials
     * 
     * @param userDTO The {@link UserDTO} containing the username and password
     * @return A {@link Response} object with an auth cookie if successful, or a
     *         401 Unauthorized status code if not
     * @author <a href="https://github.com/curdledSoy">Tom Brittenden</a>
     * 
     * 
     */
    @POST
    @Path("/login")
    public Response login(UserDTO userDTO) {
        // Get the Entity Manager
        em = getEM();
        try {
            em.getTransaction().begin();
            User u;
            NewCookie authCookie;
            try {
                try {
                    u = em.createQuery(
                        "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
                        User.class)
                    .setParameter("username", userDTO.getUsername())
                    .setParameter("password", userDTO.getPassword())
                    .getSingleResult();
                } catch (NoResultException e) {
                    // Return an unauthorized response if no user was found
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
                // If the user was found, create and persist a new session token
                authCookie = createSession(u, em);
            } finally {
                em.getTransaction().commit();
            }
            // User was found, set client's cookie to new session token
            return Response.ok().cookie(authCookie).build();
        } finally {
            em.close();
        }
    }

    // Query the database for a user given their session ID (extracted from auth
    // cookie).
    private static User getLoggedInUser(Cookie cookie, EntityManager em) {
        if (cookie == null)
            return null;
        User user;
        try {
            user = em.createQuery(
                    "SELECT user FROM User user WHERE user.session = :session",
                    User.class).setParameter("session", UUID.fromString(cookie.getValue())).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return user;
    }

    private NewCookie createSession(User user, EntityManager em) {
        user.setSession(generateSessionToken());
        em.persist(user);
        return new NewCookie(AUTH_COOKIE, user.getSession().toString());
    }

    /**
     * This method is used to get the user from the authentication cookie.
     * 
     * @param authCookie The authentication cookie of the user.
     * @param em         The entity manager used to query the database.
     * @return The user associated with the authentication cookie.
     * @author @tonneaus & <a href="https://github.com/curdledSoy">Tom Brittenden</a>
     */
    private User getUserFromCookie(Cookie authCookie, EntityManager em) {
        if (authCookie == null) return null;
        User user;
        try {
            user = em.createQuery(
                    "SELECT user FROM User user WHERE user.session = :session",
                    User.class
                ).setParameter("session", UUID.fromString(authCookie.getValue())).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return user;
    }

    private static UUID generateSessionToken() {
        UUID token = UUID.randomUUID();
        return token;
    }

    private EntityManager getEM() {
        return PersistenceManager.instance().createEntityManager();
    }

}
