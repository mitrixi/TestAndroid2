package test;

import com.github.kilianB.hash.Hash;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.HashingAlgorithm;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import com.github.kilianB.matcher.exotic.SingleImageMatcher;

import java.io.File;
import java.io.IOException;

public class CompareImg {
    public boolean compareBo(File file) throws IOException {
        File img0 = new File("/Users/mmtr/Fox.png");
        File img1 = new File("/Users/mmtr/FoxWhiteLine.jpg");

        HashingAlgorithm hasher = new PerceptiveHash(32);

        Hash hash0 = hasher.hash(img0);
        Hash hash1 = hasher.hash(img1);

        double similarityScore = hash0.normalizedHammingDistance(hash1);

        if(similarityScore < .2) {
            //Considered a duplicate in this particular case
        }

//Chaining multiple matcher for single image comparison

        SingleImageMatcher matcher = new SingleImageMatcher();
        matcher.addHashingAlgorithm(new AverageHash(64),.3);
        matcher.addHashingAlgorithm(new PerceptiveHash(32),.2);

        if(matcher.checkSimilarity(img0,img1)) {
            return true;
        }
        return false;
    }
}
