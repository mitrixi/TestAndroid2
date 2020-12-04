package service;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import com.github.kilianB.matcher.exotic.SingleImageMatcher;

import java.io.File;
import java.io.IOException;

public class ImageCompare {
    public static boolean compareBo(File screenshot, String defaultScreenshotPath) throws IOException {
        File img0 = screenshot;
        File img1 = new File(defaultScreenshotPath);
//        FileUtils.copyFile(img0, new File("/var/jenkins_home/workspace/TestAndroid/src/test/resources/screenshot/andrBoScr.jpg")); // Для сохранения/тестов

        HashingAlgorithm hasher = new PerceptiveHash(32);

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
