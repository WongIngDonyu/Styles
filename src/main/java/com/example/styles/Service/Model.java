package com.example.styles.Service;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;

public class Model {
    public float[] resizePic(String path) throws IOException {
        BufferedImage byteImage = ImageIO.read(new File(path)); //Грузим картинку
        //Меняем размер картинки на 512х512 так как это требует модель
        BufferedImage resizedImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(byteImage, 0, 0, 512, 512, null); //Перерисовываем картинку
        //Преобразуем картинку в массив
        int[] pixels = resizedImage.getRGB(0, 0, 512, 512, null, 0, 512);
        //Преобразуем в float
        float[] floatPixels = new float[pixels.length * 3]; //3 канала (RGB) на каждый пиксель
        for (int i = 0; i < pixels.length; ++i) { //перебираем пиксели
            int pixel = pixels[i];
            floatPixels[i * 3] = (float) ((pixel >> 16) & 0xFF); //Red
            floatPixels[i * 3 + 1] = (float) ((pixel >> 8) & 0xFF); //Green
            floatPixels[i * 3 + 2] = (float) ((pixel) & 0xFF); //Blue
        }
        return floatPixels;
    }

    public String getAnswer(String path, SavedModelBundle model) throws IOException {
        float[] Picture = resizePic(path);
        //Создаем тензор
        Tensor<Float> tensor = Tensor.create(new long[]{1, 512, 512, 3}, FloatBuffer.wrap(Picture)); //1-количество картинок, 512-размер картинки, 3-количество каналов
        //Получаем результат
        Tensor<?> result = model.session()
                .runner()
                .feed("serving_default_sequential_8_input:0", tensor) //Входной тензор
                .fetch("StatefulPartitionedCall:0") //Выходной тензор
                .run()
                .get(0);
        //Выводим результат в массив 1-5, так как всего 5 классов
        float[][] resultArray = new float[1][5];
        result.copyTo(resultArray);

        //Получаем индекс максимального значения
        int index = 0;
        float max = resultArray[0][0];
        for (int i = 0; i < resultArray[0].length; i++) {
            if (resultArray[0][i] > max) {
                max = resultArray[0][i];
                index = i;
            }
        }

        //Возвращаем результат
        System.out.println(resultArray[0][index]);


        for (int i = 0; i < resultArray[0].length; i++) {
            System.out.println(resultArray[0][i]);
        }

        switch (index) {
            case 0:
                return "Baroque";
            case 1:
                return "Bauhaus";
            case 2:
                return "Edwardian";
            case 3:
                return "Gothic";
            case 4:
                return "Postmodern";
            default:
                return "Error";
        }
    }
}
