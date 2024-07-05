# Team members & organisation

Team discussion was done in GitHub issues.  Everyone basically particated about
the same for everything.  Everyone worked on HTTP endpoints and mappers;
`tbri867` and `zlu632` worked on the domain model.

## Details

`jbre983` @tonneaus: worked on `ConcertResource`: implemented bookings endpoints
(`getBookings`, `makeBookings`) and seats endpoint (`getSeats`); made sure
double bookings were not possible; mappers (`BookingMapper`); date
serialization; built on work by other members getting unit tests to pass (e.g.
async subscription logic).

`zlu632` @edenlzh: worked on domain model, mappers, and `ConcertResource`:
subscription endpoint.

`tbri867` @curdledSoy: worked on domain model, mappers, date serialization, and
`ConcertResource`: implemented concert endpoints (`getSingleConcert`,
`getAllConcerts`, `getAllConcertSummaries`), performer endpoints
(`getAllPerformers`, `getSinglePerformer`), and subscription endpoint and async
subscription logic.

# Concurrency errors

We minimise the chance of concurrency errors by enclosing every response
function inside a database transaction.  Database transactions are at the Read
Committed isolation level which allows unrepeatable reads and phantom reads.

Concurrency inconsistency considerations are most important for things that
mutate the database.  The only functionalities that mutate the database are
logging in and making a booking.  Concurrency considerations are not really
important for this functionality, since if there is a race condition between two
concurrenct transactions, one will win and there will be no side effects.  (Each
user can have only one session at a time.)  Also this functionality does not
read from the database, so the limitations of the isolation level don't matter.

Making a booking is different however since we want to prevent double bookings.
Each seat can be booked by at most one booking, due to a one-to-many
(`@OneToMany`) assocation between `Booking`s and `Seat`s.  This introduces a
foregin key constraint to the `Seat` table, preventing simultaneous double
bookings, but not preventing the seat from being stolen by a subsequent booking.
To prevent clobbering double bookings we use the `OPTIMISTIC_FORCE_INCREMENT`
lock mode type when querying seats while making a new booking.  This guarantees
repeatable reads which is the desired property here.  Since we keep track of
whether a seat is booked or not (`Seat.isBooked`) and we set this to true once a
booking has been made with a particular seat, if this attribute is concurrently
modified by another transaction, the current transaction will encounter an error
and roll back, thereby preventing a double booking.

# Organisation of domain model

Each object type (booking, concert, performer, seat, user) has its own class in
the domain model.  The domain model always uses lazy loading over eager loading;
this should be beneficial for scaling the application.
