package com.example.rootdeng.Controller;

import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;

import java.io.File;
import java.io.IOException;

public class Analyzer {

    public static final String inputPath="/media/data/test/ChnSentiCorp情感分析酒店评论";
    NaiveBayesClassifier classifier=new NaiveBayesClassifier();

    public static void main(String[] args) throws IOException {

        NaiveBayesClassifier classifier=new NaiveBayesClassifier();

        classifier.train(inputPath);
        //情感分析
        poredic(classifier,"精妙绝伦");
    }
    //预处理
    public void  Preprocessing() throws IOException {
        classifier.train(inputPath);
    }

    //情感分析
    public int calculate(String text) throws IOException {
        classifier.train(inputPath);
        String label=classifier.classify(text);
        System.out.println("the label is "+label);
        if(label.compareTo("正面")==0){
            return 1;
        }else if(label.compareTo("负面")==0){
            return 2;
        }else{
            return 0;
        }
    }


    private static void poredic(IClassifier classifier,String text){
    //    System.out.println(classifier.classify(text));
    //    System.out.printf("《%S》情感是[%s]\n",text,classifier.classify(text));
    }

    //验证数据是否存在
    static{
        File file=new File(inputPath);
        if(!file.exists()||!file.isDirectory()){
            System.out.println(file.getPath());
            System.err.println("没有文本分类语料，请设置正确的语料格式");
            System.exit(1);
        }
    }

}
