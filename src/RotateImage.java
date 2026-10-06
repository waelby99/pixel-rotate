import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class RotateImage {

    public static void rotate(int[][] matrix) {

        int len = matrix.length;

        // 1. Transpose
        for (int i = 0; i < len; i++) {
            for (int j = i; j < len; j++) {

                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        // 2. Reverse every row
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len / 2; j++) {

                int temp = matrix[i][j];

                matrix[i][j] = matrix[i][len - j - 1];

                matrix[i][len - j - 1] = temp;
            }
        }
    }

    public static void main(String[] args) throws Exception {

        BufferedImage image = ImageIO.read(new File("src/input.png"));

        int width = image.getWidth();
        int height = image.getHeight();

        if (width != height) {
            System.out.println("Please use a square image.");
            return;
        }

        int[][] pixels = new int[height][width];

        // Image -> int[][]
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                pixels[i][j] = image.getRGB(j, i);
            }
        }
        // Solution to the Leetcode algorithm
        rotate(pixels);

        // int[][] -> new image
        BufferedImage rotated = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rotated.setRGB(j, i, pixels[i][j]);
            }
        }

        ImageIO.write(rotated, "png", new File("rotated.png"));
    }
}