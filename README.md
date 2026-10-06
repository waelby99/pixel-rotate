# PixelRotate

### Rotating a real image with the same matrix algorithm I used on LeetCode

PixelRotate is a small Java experiment where I took the classic **Rotate Image** matrix problem and applied the same idea to an actual image.

The core idea is simple:

> **An image is just a 2D matrix of pixels.**

So instead of rotating numbers inside an `int[][]`, I can store image pixels inside an `int[][]`, apply the exact same algorithm, and rebuild the image afterward.

I love building small solutions like this. Taking a simple algorithm, experimenting with it, and turning it into something visual and practical is part of who I am as a developer.

---

## Before and After

### Original image

![Original image before rotation](src/input.png)

### Rotated image — 90° clockwise

![Image after rotation](rotated.png)

---

## How I Came Up With the Idea

I was solving the classic **Rotate Image** problem on LeetCode.

The problem gives us a square matrix and asks us to rotate it **90 degrees clockwise in place**.

My solution uses two operations:

1. **Transpose the matrix**
2. **Reverse each row by swapping columns**

The original solution looks like this:

```java
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
```

While working on it, I realized something:

A digital image can also be represented as a matrix.

Instead of this:

```text
1 2 3
4 5 6
7 8 9
```

we can think of an image as:

```text
pixel pixel pixel
pixel pixel pixel
pixel pixel pixel
```

That made me wonder:

> What happens if I put every pixel into an `int[][]` and run my exact LeetCode algorithm on it?

That is how this project started.

---

# How the Algorithm Works

## Step 1 — Transpose the Matrix

Suppose we start with:

```text
1 2 3
4 5 6
7 8 9
```

The transpose swaps:

```text
matrix[i][j]
```

with:

```text
matrix[j][i]
```

After transposing:

```text
1 4 7
2 5 8
3 6 9
```

The Java code is:

```java
for (int i = 0; i < len; i++) {
    for (int j = i; j < len; j++) {

        int temp = matrix[i][j];

        matrix[i][j] = matrix[j][i];

        matrix[j][i] = temp;
    }
}
```

### Why does `j` start at `i`?

This part matters.

If I used:

```java
for (int j = 0; j < len; j++)
```

I would swap some elements twice.

For example:

```text
matrix[0][1] <-> matrix[1][0]
```

Later, I would reach:

```text
matrix[1][0] <-> matrix[0][1]
```

and undo the first swap.

Starting from:

```java
j = i
```

means I only process one half of the matrix.

---

# Step 2 — Reverse Every Row

After the transpose:

```text
1 4 7
2 5 8
3 6 9
```

I reverse each row:

```text
7 4 1
8 5 2
9 6 3
```

Now the matrix is rotated **90° clockwise**.

The code:

```java
for (int i = 0; i < len; i++) {
    for (int j = 0; j < len / 2; j++) {

        int temp = matrix[i][j];

        matrix[i][j] = matrix[i][len - j - 1];

        matrix[i][len - j - 1] = temp;
    }
}
```

---

## Understanding the Column Swap

The expression:

```java
len - j - 1
```

gives the column on the opposite side of the row.

For a row of length `5`:

```text
index:  0  1  2  3  4
```

When:

```text
j = 0
```

the matching index is:

```text
5 - 0 - 1 = 4
```

So:

```text
0 <-> 4
```

When:

```text
j = 1
```

the matching index is:

```text
5 - 1 - 1 = 3
```

So:

```text
1 <-> 3
```

The middle element does not need to move.

That is why the loop only runs until:

```java
len / 2
```

---

# Turning an Image Into a Matrix

Java's `BufferedImage` lets us access individual pixels.

A pixel can be read with:

```java
image.getRGB(x, y)
```

and Java returns the pixel as an `int`.

That means I can create:

```java
int[][] pixels = new int[height][width];
```

and copy the image into it:

```java
for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {

        pixels[i][j] = image.getRGB(j, i);
    }
}
```

The mapping is:

```text
i = row    = y
j = column = x
```

So:

```java
pixels[i][j]
```

corresponds to:

```java
image.getRGB(j, i)
```

---

# Why Can a Pixel Be Stored as an `int`?

`BufferedImage.getRGB()` returns the color of a pixel as an integer.

That integer contains color-channel information:

```text
A R G B
```

which stands for:

- Alpha
- Red
- Green
- Blue

My rotation algorithm does not care what the integer represents.

It only moves values from one position to another.

That means the exact same matrix algorithm can move pixels instead of normal numbers.

---

# Applying My LeetCode Algorithm

Once the image has been converted into:

```java
int[][]
```

I simply call:

```java
rotate(pixels);
```

The algorithm does not need to know that the values are pixels.

To it, this:

```text
1 2 3
4 5 6
7 8 9
```

and this:

```text
pixel pixel pixel
pixel pixel pixel
pixel pixel pixel
```

are both just 2D arrays of integers.

That is the part I liked most about this experiment.

The **data changed**, but the **algorithm stayed the same**.

---

# Rebuilding the Image

After rotating the pixel matrix, I create a new image:

```java
BufferedImage rotated = new BufferedImage(
        width,
        height,
        BufferedImage.TYPE_INT_ARGB
);
```

Then I copy the rotated pixels back:

```java
for (int i = 0; i < height; i++) {
    for (int j = 0; j < width; j++) {

        rotated.setRGB(
                j,
                i,
                pixels[i][j]
        );
    }
}
```

Finally:

```java
ImageIO.write(
        rotated,
        "png",
        new File("rotated.png")
);
```

The program generates the rotated image as:

```text
rotated.png
```

---

# Full Java Program

```java
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

        BufferedImage image =
                ImageIO.read(new File("src/input.png"));

        int width = image.getWidth();
        int height = image.getHeight();

        if (width != height) {
            System.out.println(
                    "For this exact LeetCode algorithm, use a square image."
            );
            return;
        }

        int[][] pixels = new int[height][width];

        // Image -> matrix
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                pixels[i][j] = image.getRGB(j, i);
            }
        }

        // Same LeetCode algorithm
        rotate(pixels);

        BufferedImage rotated =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB
                );

        // Matrix -> image
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                rotated.setRGB(
                        j,
                        i,
                        pixels[i][j]
                );
            }
        }

        ImageIO.write(
                rotated,
                "png",
                new File("rotated.png")
        );

        System.out.println("Done!");
    }
}
```

---

# Complexity

For an `n x n` image:

### Time Complexity

The transpose takes:

```text
O(n²)
```

Reversing all rows also takes:

```text
O(n²)
```

So overall:

```text
O(n²)
```

That is expected because every pixel needs to be visited.

### Space Complexity

The matrix rotation itself is:

```text
O(1)
```

extra space because the swaps are done in place.

However, this project first copies the image into an `int[][]`, so the complete image-processing program uses:

```text
O(n²)
```

additional memory for the pixel matrix.

---

# Project Structure

```text
PixelRotate/
│
├── src/
│   ├── RotateImage.java
│   └── input.png
│
├── rotated.png
├── README.md
└── .gitignore
```

---

# Technologies

- Java 17
- `BufferedImage`
- `ImageIO`
- 2D arrays
- Matrix manipulation
- Basic image processing

---

# Current Limitation

This project intentionally uses the exact logic from the LeetCode square-matrix problem.

Because of that, the current implementation expects:

```text
width == height
```

So the input image must be square.

A future version could support rectangular images by creating a new matrix where:

```text
new width  = old height
new height = old width
```

---

# What I Learned

This small project reinforced a few things for me:

- An image can be treated as a matrix of pixels.
- A pixel can be represented as an integer.
- Matrix algorithms can have practical uses outside coding challenges.
- Transpose + reverse rows produces a 90° clockwise rotation.
- Understanding an algorithm is more useful than memorizing it.
- Sometimes the same algorithm can solve a completely different-looking problem.

---

# Why I Built This

This is not meant to be a huge application.

It is a small experiment that came directly from curiosity.

I was solving an algorithm problem, understood what the transformations were doing, and wanted to see whether I could apply the same idea to something real and visual.

I love building small solutions like this.

Taking an idea, understanding it, experimenting with it, and making something work is part of who I am as a developer.

---

## Final Thought

The coolest part is that the rotation algorithm itself never needed to know anything about images.

On LeetCode, the matrix contained numbers.

Here, the matrix contains pixels.

The data changed.

The algorithm did not.

```text
Transpose + reverse rows = 90° clockwise rotation
```
