# Built in packages
# noinspection PyUnresolvedReferences
import math
import sys
from pathlib import Path

# Matplotlib will need to be installed if it isn't already. This is the only package allowed for this base part of the
# assignment.
from matplotlib import pyplot
from matplotlib.patches import Rectangle

# import our basic, light-weight png reader library
import imageIO.png

# this function reads an RGB color png file and returns width, height, as well as pixel arrays for r,g,b
def readRGBImageToSeparatePixelArrays(input_filename):

    image_reader = imageIO.png.Reader(filename=input_filename)
    # png reader gives us width and height, as well as RGB data in image_rows (a list of rows of RGB triplets)
    (image_width, image_height, rgb_image_rows, rgb_image_info) = image_reader.read()

    print("read image width={}, height={}".format(image_width, image_height))

    # our pixel arrays are lists of lists, where each inner list stores one row of greyscale pixels
    pixel_array_r = []
    pixel_array_g = []
    pixel_array_b = []

    for row in rgb_image_rows:
        pixel_row_r = []
        pixel_row_g = []
        pixel_row_b = []
        r = 0
        g = 0
        b = 0
        for elem in range(len(row)):
            # RGB triplets are stored consecutively in image_rows
            if elem % 3 == 0:
                r = row[elem]
            elif elem % 3 == 1:
                g = row[elem]
            else:
                b = row[elem]
                pixel_row_r.append(r)
                pixel_row_g.append(g)
                pixel_row_b.append(b)

        pixel_array_r.append(pixel_row_r)
        pixel_array_g.append(pixel_row_g)
        pixel_array_b.append(pixel_row_b)

    return (image_width, image_height, pixel_array_r, pixel_array_g, pixel_array_b)


# a useful shortcut method to create a list of lists based array representation for an image, initialized with a value
def createInitializedGreyscalePixelArray(image_width, image_height, initValue = 0):

    new_array = [[initValue for x in range(image_width)] for y in range(image_height)]
    return new_array


# You can add your own functions here:
def computeRGBToGreyscale(pixel_array_r, pixel_array_g, pixel_array_b, image_width, image_height):
    greyscale_pixel_array = createInitializedGreyscalePixelArray(image_width, image_height)

    for i in range(image_height):
        for j in range(image_width):
            greyscale_pixel_array[i][j] = round(
                0.299 * pixel_array_r[i][j] + 0.587 * pixel_array_g[i][j] + 0.114 * pixel_array_b[i][j])

    return greyscale_pixel_array

def computeMinAndMaxValues(pixel_array, image_width, image_height):
    min_value = pixel_array[0][0]
    max_value = pixel_array[0][0]
    for i in range(image_height):
        min_value1 = min(pixel_array[i])
        max_value1 = max(pixel_array[i])
        if min_value1 < min_value:
            min_value = min_value1
        if max_value1 > max_value:
            max_value = max_value1

    tuple_1 = (min_value, max_value)
    return tuple_1


def scaleTo0And255AndQuantize(pixel_array, image_width, image_height):
    greyscale = createInitializedGreyscalePixelArray(image_width, image_height)
    k = computeMinAndMaxValues(pixel_array, image_width, image_height)
    if k[0] != k[1]:
        for i in range(image_height):
            for j in range(image_width):
                greyscale[i][j] = round((pixel_array[i][j] - k[0]) * (255 / (k[1] - k[0])))
                if greyscale[i][j] < 0:
                    greyscale[i][j] = 0
                if greyscale[i][j] > 255:
                    greyscale[i][j] = 255

    return greyscale


def computeEdgesSobelAbsolute(pixel_array, image_width, image_height):
    greyscale = createInitializedGreyscalePixelArray(image_width, image_height)
    greyscale1 = createInitializedGreyscalePixelArray(image_width, image_height)
    greyscale2 = createInitializedGreyscalePixelArray(image_width, image_height)
    for i in range(image_height):
        for j in range(image_width):
            if (i != 0 and i != image_height-1 and j != 0 and j != image_width-1):
                k = ((pixel_array[i - 1][j - 1] + pixel_array[i + 1][j - 1]) * (-1) + (-2) * pixel_array[i][j - 1] + (
                            pixel_array[i - 1][j + 1] + pixel_array[i + 1][j + 1]) + 2 * pixel_array[i][j + 1]) / 8
                if (k < 0):
                    k = -k
                greyscale[i][j] = k
            else:
                greyscale[i][j] = 0.0

    for i in range(image_height):
        for j in range(image_width):
            if (i != 0 and i != image_height-1 and j != 0 and j != image_width-1):
                k = ((pixel_array[i + 1][j - 1] + pixel_array[i + 1][j + 1]) * (-1) + (-2) * pixel_array[i + 1][j] + (
                            pixel_array[i - 1][j - 1] + pixel_array[i - 1][j + 1]) + 2 * pixel_array[i - 1][j]) / 8
                if (k < 0):
                    k = -k
                greyscale1[i][j] = k
            else:
                greyscale1[i][j] = 0.0

    for i in range(image_height):
        for j in range(image_width):
            k = abs(greyscale1[i][j]-greyscale[i][j])
            greyscale2[i][j] = k

    return greyscale2


def computeStandardDeviationImage3x3(pixel_array, image_width, image_height):
    greyscale = createInitializedGreyscalePixelArray(image_width, image_height)
    for i in range(image_height):
        for j in range(image_width):
            if (i >= 2 and i < image_height - 2 and j >= 2 and j < image_width - 2):
                mean = (pixel_array[i-2][j-2]+pixel_array[i-2][j-1]+pixel_array[i-2][j]+pixel_array[i-2][j+1]+pixel_array[i-2][j+2]+
                        pixel_array[i-1][j-2]+pixel_array[i-1][j-1]+pixel_array[i-1][j]+pixel_array[i-1][j+1]+pixel_array[i-1][j+2]+
                        pixel_array[i][j-2]+pixel_array[i][j-1]+pixel_array[i][j]+pixel_array[i][j+1]+pixel_array[i][j+2]+
                        pixel_array[i+1][j-2]+pixel_array[i+1][j-1]+pixel_array[i+1][j]+pixel_array[i+1][j+1]+pixel_array[i+1][j+2]+
                        pixel_array[i+2][j-2]+pixel_array[i+2][j-1]+pixel_array[i+2][j]+pixel_array[i+2][j+1]+pixel_array[i+2][j+2]) / 25
                k = math.sqrt(((pixel_array[i-2][j-2]-mean)**2+(pixel_array[i-2][j-1]-mean)**2+(pixel_array[i-2][j]-mean)**2+(pixel_array[i-2][j+1]-mean)**2+(pixel_array[i-2][j+2]-mean)**2+
                            (pixel_array[i-1][j-2]-mean)**2+(pixel_array[i-1][j-1]-mean)**2+(pixel_array[i-1][j]-mean)**2+(pixel_array[i-1][j+1]-mean)**2+(pixel_array[i-1][j+2]-mean)**2+
                            (pixel_array[i][j-2]-mean)**2+(pixel_array[i][j-1]-mean)**2+(pixel_array[i][j]-mean)**2+(pixel_array[i][j+1]-mean)**2+(pixel_array[i][j+2]-mean)**2+
                            (pixel_array[i+1][j-2]-mean)**2+(pixel_array[i+1][j-1]-mean)**2+(pixel_array[i+1][j]-mean)**2+(pixel_array[i+1][j+1]-mean)**2+(pixel_array[i+1][j+2]-mean)**2+
                            (pixel_array[i+2][j-2]-mean)**2+(pixel_array[i+2][j-1]-mean)**2+(pixel_array[i+2][j]-mean)**2+(pixel_array[i+2][j+1]-mean)**2+(pixel_array[i+2][j+2]-mean)**2) / 25)

                greyscale[i][j] = k
            else:
                greyscale[i][j] = 0.0

    return greyscale


def computeGaussianAveraging3x3RepeatBorder(pixel_array, image_width, image_height):
    greyscale = createInitializedGreyscalePixelArray(image_width + 2, image_height + 2)
    for i in range(1, image_height + 1):
        for j in range(1, image_width + 1):
            greyscale[i][j] = pixel_array[i - 1][j - 1]

    for i in range(1, image_height + 1):
        for j in range(1, image_width + 1):
            if (i == 1 and j == 1):
                greyscale[i - 1][j - 1] = greyscale[i][j]
                greyscale[i - 1][j] = greyscale[i][j]
                greyscale[i][j - 1] = greyscale[i][j]
            elif (i == 1 and j == image_width):
                greyscale[i - 1][j + 1] = greyscale[i][j]
                greyscale[i - 1][j] = greyscale[i][j]
                greyscale[i][j + 1] = greyscale[i][j]
            elif (i == image_height and j == 1):
                greyscale[i + 1][j - 1] = greyscale[i][j]
                greyscale[i][j - 1] = greyscale[i][j]
                greyscale[i + 1][j] = greyscale[i][j]
            elif (i == image_height and j == image_width):
                greyscale[i + 1][j + 1] = greyscale[i][j]
                greyscale[i + 1][j] = greyscale[i][j]
                greyscale[i][j + 1] = greyscale[i][j]
            elif (i == 1):
                greyscale[i - 1][j] = greyscale[i][j]
            elif (i == image_height):
                greyscale[i + 1][j] = greyscale[i][j]
            elif (j == 1):
                greyscale[i][j - 1] = greyscale[i][j]
            elif (j == image_width):
                greyscale[i][j + 1] = greyscale[i][j]

    if (image_height == image_width == 1):
        pixel_array[0][0] = float(pixel_array[0][0])
    else:
        for i in range(1, image_height + 1):
            for j in range(1, image_width + 1):
                k = (greyscale[i - 1][j - 1] + 2 * greyscale[i - 1][j] + greyscale[i - 1][j + 1] + 2 * greyscale[i][
                    j - 1] + 4 * greyscale[i][j] + 2 * greyscale[i][j + 1] + greyscale[i + 1][j - 1] + 2 *
                     greyscale[i + 1][j] + greyscale[i + 1][j + 1]) / 16
                pixel_array[i - 1][j - 1] = k

    return pixel_array

def computeThreshold(pixel_array, image_width, image_height):
    threshold = 25
    for i in range(image_height):
        for j in range(image_width):
            if (pixel_array[i][j] < threshold):
                pixel_array[i][j] = 0.0
            else:
                pixel_array[i][j] = 1.0

    return pixel_array


def computeErosion8Nbh5x5FlatSE(pixel_array, image_width, image_height):
    greyscale = createInitializedGreyscalePixelArray(image_width + 4, image_height + 4)
    for i in range(2, image_height + 2):
        for j in range(2, image_width + 2):
            greyscale[i][j] = pixel_array[i - 2][j - 2]

    for i in range(2, image_height + 2):
        for j in range(2, image_width + 2):
            if (greyscale[i - 2][j - 2] == 0 or greyscale[i - 2][j - 1] == 0 or greyscale[i - 2][j] == 0 or greyscale[i - 2][j + 1] == 0 or greyscale[i - 2][j + 2] == 0 or
                greyscale[i - 1][j - 2] == 0 or greyscale[i - 1][j - 1] == 0 or greyscale[i - 1][j] == 0 or greyscale[i - 1][j + 1] == 0 or greyscale[i - 1][j + 2] == 0 or
                greyscale[i][j - 2] == 0 or greyscale[i][j - 1] == 0 or greyscale[i][j] == 0 or greyscale[i][j + 1] == 0 or greyscale[i][j + 2] == 0 or
                greyscale[i + 1][j - 2] == 0 or greyscale[i + 1][j - 1] == 0 or greyscale[i + 1][j] == 0 or greyscale[i + 1][j + 1] == 0 or greyscale[i + 1][j + 2] == 0 or
                greyscale[i + 2][j - 2] == 0 or greyscale[i + 2][j - 1] == 0 or greyscale[i + 2][j] == 0 or greyscale[i + 2][j + 1] == 0 or greyscale[i + 2][j + 2] == 0):
                pixel_array[i - 2][j - 2] = 0
            else:
                pixel_array[i - 2][j - 2] = 1

    return pixel_array


def computeDilation8Nbh5x5FlatSE(pixel_array, image_width, image_height):
    greyscale = createInitializedGreyscalePixelArray(image_width + 4, image_height + 4)
    for i in range(2, image_height + 2):
        for j in range(2, image_width + 2):
            greyscale[i][j] = pixel_array[i - 2][j - 2]

    for i in range(2, image_height + 2):
        for j in range(2, image_width + 2):
            if (greyscale[i - 2][j - 2] != 0 or greyscale[i - 2][j - 1] != 0 or greyscale[i - 2][j] != 0 or greyscale[i - 2][j + 1] != 0 or greyscale[i - 2][j + 2] != 0 or
                greyscale[i - 1][j - 2] != 0 or greyscale[i - 1][j - 1] != 0 or greyscale[i - 1][j] != 0 or greyscale[i - 1][j + 1] != 0 or greyscale[i - 1][j + 2] != 0 or
                greyscale[i][j - 2] != 0 or greyscale[i][j - 1] != 0 or greyscale[i][j] != 0 or greyscale[i][j + 1] != 0 or greyscale[i][j + 2] != 0 or
                greyscale[i + 1][j - 2] != 0 or greyscale[i + 1][j - 1] != 0 or greyscale[i + 1][j] != 0 or greyscale[i + 1][j + 1] != 0 or greyscale[i + 1][j + 2] != 0 or
                greyscale[i + 2][j - 2] != 0 or greyscale[i + 2][j - 1] != 0 or greyscale[i + 2][j] != 0 or greyscale[i + 2][j + 1] != 0 or greyscale[i + 2][j + 2] != 0):
                pixel_array[i - 2][j - 2] = 1

    return pixel_array

class Queue:
    def __init__(self):
        self.items = []

    def isEmpty(self):
        return self.items == []

    def enqueue(self, item):
        self.items.insert(0,item)

    def dequeue(self):
        return self.items.pop()

    def size(self):
        return len(self.items)


def computeConnectedComponentLabeling(pixel_array, image_width, image_height):
    result_image = createInitializedGreyscalePixelArray(image_width, image_height);
    count = [];
    inside_count = 0;
    label_number = 1;
    neighbor = [(-1, 0), (0, -1), (0, 1), (1, 0)];
    q = Queue();
    count_enqueue = 0;
    i = 0;
    j = 0;
    while image_height > j:
        while image_width > i:
            if pixel_array[j][i] > 0 and result_image[j][i] == 0:
                q.enqueue((j, i));
                while not q.isEmpty():
                    n, m = q.dequeue();
                    inside_count += 1;
                    result_image[n][m] = label_number;
                    for xi, eta in neighbor:
                        x = m + xi;
                        y = n + eta;
                        if 0 <= y < image_height and 0 <= x < image_width:
                            if pixel_array[y][x] > 0 and result_image[y][x] == 0:
                                count_enqueue += 1;
                                result_image[y][x] = label_number
                                q.enqueue((y, x))

                count = count + [inside_count];
                inside_count = 0;
                label_number += 1;

            i += 1;
        i = 0;
        j += 1;
    length = len(count)
    leng = [0 for x in range(length)]

    i = 0
    while i < length:
        leng[i] = i + 1
        i += 1
    return (result_image, dict(zip(leng, count)))


def get_keys(dic, value):
    return [k for k, v in dic.items() if v == value]

def computeMaxAndMinCoordinates(pixel_array, image_width, image_height, dic):
    x = []
    y = []
    k = len(dic)
    while (k>0):
        k = len(dic)
        max_value = max(dic.values())
        key_value = get_keys(dic,max_value)
        for i in range(image_height):
            for j in range(image_width):
                if (pixel_array[i][j] == key_value[0]):
                    x.append(j)
                    y.append(i)
        min_x = min(x)
        min_y = min(y)
        max_x = max(x)
        max_y = max(y)
        a = max_x - min_x
        b = max_y - min_y
        if (a<=b and a!= 0):
            ratio = b/a
        if (a>b and b!= 0):
            ratio = a/b
        if (ratio >= 1.8):
            del dic[key_value]
            x=[]
            y=[]
        else:
            location = [min_x, min_y, max_x, max_y]
            return location


def separateArraysToRGB(px_array_r, px_array_g, px_array_b, image_width, image_height):
    new_array = [[[0 for c in range(3)] for x in range(image_width)] for y in range(image_height)]
    for y in range(image_height):
        for x in range(image_width):
            new_array[y][x][0] = px_array_r[y][x]
            new_array[y][x][1] = px_array_g[y][x]
            new_array[y][x][2] = px_array_b[y][x]

    return new_array




# This is our code skeleton that performs the barcode detection.
# Feel free to try it on your own images of barcodes, but keep in mind that with our algorithm developed in this assignment,
# we won't detect arbitrary or difficult to detect barcodes!
def main():

    command_line_arguments = sys.argv[1:]

    SHOW_DEBUG_FIGURES = True

    # this is the default input image filename
    filename = "Barcode3"
    input_filename = "images/"+filename+".png"

    if command_line_arguments != []:
        input_filename = command_line_arguments[0]
        SHOW_DEBUG_FIGURES = False

    output_path = Path("output_images")
    if not output_path.exists():
        # create output directory
        output_path.mkdir(parents=True, exist_ok=True)

    output_filename = output_path / Path(filename+"_output.png")
    if len(command_line_arguments) == 2:
        output_filename = Path(command_line_arguments[1])

    # we read in the png file, and receive three pixel arrays for red, green and blue components, respectively
    # each pixel array contains 8 bit integer values between 0 and 255 encoding the color values
    (image_width, image_height, px_array_r, px_array_g, px_array_b) = readRGBImageToSeparatePixelArrays(input_filename)

    # setup the plots for intermediate results in a figure
    fig1, axs1 = pyplot.subplots(2, 2)
    axs1[0, 0].set_title('Input red channel of image')
    axs1[0, 0].imshow(px_array_r, cmap='gray')
    axs1[0, 1].set_title('Input green channel of image')
    axs1[0, 1].imshow(px_array_g, cmap='gray')
    axs1[1, 0].set_title('Input blue channel of image')
    axs1[1, 0].imshow(px_array_b, cmap='gray')


    # STUDENT IMPLEMENTATION here
    # px_array1 is for the part 1 of the assignment
    px_array1 = computeRGBToGreyscale(px_array_r, px_array_g, px_array_b, image_width, image_height)
    #px_array1 = scaleTo0And255AndQuantize(computeRGBToGreyscale(px_array_r, px_array_g, px_array_b, image_width, image_height), image_width, image_height)
    # px_array2 is for the 2a of the assignment
    px_array2 = computeEdgesSobelAbsolute(px_array1, image_width, image_height)
    #px_array3 is for the 2b
    px_array3 = computeStandardDeviationImage3x3(px_array1, image_width, image_height)
    # px_array4 is for the part 3 of the assignment
    px_array4 = computeGaussianAveraging3x3RepeatBorder(computeGaussianAveraging3x3RepeatBorder(computeGaussianAveraging3x3RepeatBorder(computeGaussianAveraging3x3RepeatBorder(px_array2,image_width,image_height), image_width, image_height),image_width,image_height),image_width,image_height)
    # px_array5 is for the part 4 of the assignment
    px_array5 = computeThreshold(px_array4,image_width,image_height)
    # px_array7 is for the part 5 of the assignment
    px_array6 = computeDilation8Nbh5x5FlatSE(computeDilation8Nbh5x5FlatSE(px_array5, image_width, image_height), image_width, image_height)
    px_array7 = computeErosion8Nbh5x5FlatSE(computeErosion8Nbh5x5FlatSE(computeErosion8Nbh5x5FlatSE(px_array6, image_width, image_height), image_width, image_height), image_width,image_height)
    # px_array8 is for the part 6 of the assignment
    example = computeConnectedComponentLabeling(px_array7, image_width, image_height)
    px_array8 = example[0]
    location = computeMaxAndMinCoordinates(px_array8,image_width,image_height,example[1])
    px_array = separateArraysToRGB(px_array_r, px_array_g, px_array_b, image_width, image_height)



    # Compute a dummy bounding box centered in the middle of the input image, and with as size of half of width and height
    # Change these values based on the detected barcode region from your algorithm
    center_x = image_width / 2.0
    center_y = image_height / 2.0
    bbox_min_x = location[0]
    bbox_max_x = location[2]
    bbox_min_y = location[1]
    bbox_max_y = location[3]

    # The following code is used to plot the bounding box and generate an output for marking
    # Draw a bounding box as a rectangle into the input image
    axs1[1, 1].set_title('Final image of detection')
    axs1[1, 1].imshow(px_array, cmap='gray')
    rect = Rectangle((bbox_min_x, bbox_min_y), bbox_max_x - bbox_min_x, bbox_max_y - bbox_min_y, linewidth=1,
                     edgecolor='g', facecolor='none')
    axs1[1, 1].add_patch(rect)

    # write the output image into output_filename, using the matplotlib savefig method
    extent = axs1[1, 1].get_window_extent().transformed(fig1.dpi_scale_trans.inverted())
    pyplot.savefig(output_filename, bbox_inches=extent, dpi=600)

    if SHOW_DEBUG_FIGURES:
        # plot the current figure
        pyplot.show()


if __name__ == "__main__":
    main()