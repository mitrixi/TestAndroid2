package service;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import com.github.kilianB.matcher.exotic.SingleImageMatcher;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CompareImg {
    public boolean compareBo(File screenshot, String defaultScreenshotPath) throws IOException {
        System.out.println(5);

        File img0 = screenshot;
        System.out.println("6");

        File img1 = new File(defaultScreenshotPath);
        System.out.println("7");
        FileUtils.copyFile(img0, new File("/home/mitrixi/Pictures/andrBoScr.jpg")); // Для сохранения/тестов
        System.out.println(8);

        HashingAlgorithm hasher = new PerceptiveHash(32);
        System.out.println(9);

        Hash hash0 = hasher.hash(img0);
        Hash hash1 = hasher.hash(img1);

        double similarityScore = hash0.normalizedHammingDistance(hash1);

        if (similarityScore < .2) {
            //Considered a duplicate in this particular case
            return true;
        }

        //Chaining multiple matcher for single image comparison

        SingleImageMatcher matcher = new SingleImageMatcher();
        matcher.addHashingAlgorithm(new AverageHash(64), .3);
        matcher.addHashingAlgorithm(new PerceptiveHash(32), .2);

        return matcher.checkSimilarity(img0, img1);
    }
}
