package com.example.backendproject.program;

import com.example.backendproject.entity.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class FindInfo {
    public FindInfo(){

    }

    private Transactions transactions = new Transactions();
    private  Locale trlocale = new Locale("tr", "TR");
    private ArrayList<String> lineListLowerCase = new ArrayList<>();
    private ArrayList<String> lineList = new ArrayList<>();

    public void findFromPdf(String filePath , int selectedUserId){
        readPdf(filePath);
        String[]keyWordList=findKeyWords(lineListLowerCase);
        ArrayList<Mentor> mentorList=findMentorInfo(lineListLowerCase,lineList);
        String abstractForPdf=findAbstract(lineListLowerCase);
        String lessonType=findLessonType(lineListLowerCase);
        ArrayList<Author> authorList=findAuthorInfo(lineListLowerCase,lineList);
        String projectTitle=findProjectTitle(lineListLowerCase,lineList,lessonType,authorList);
        DeliveryPeriod deliveryPeriod=findDeliveryPeriod(lineListLowerCase);
        writeInfo(keyWordList,mentorList,abstractForPdf,lessonType,authorList,projectTitle,deliveryPeriod,filePath,selectedUserId);

    }
    public void writeInfo(String[]keyWordList, ArrayList<Mentor> mentorList,String abstractForPdf
                           , String lessonType,ArrayList<Author> authorList,String projectTitle,
                          DeliveryPeriod deliveryPeriod,String filePath , int selectedUserId){
        /*writeAbstract(abstractForPdf);
        writeAuthorInfo(authorList);
        writeProjectTitle(projectTitle);
        writeMentorInfo(mentorList);
        writeLessonType(lessonType);
        writeKeyWordList(keyWordList);*/
        Project project = new Project();
        project.setName(projectTitle.trim());
        project.setDeliveryPeriod(deliveryPeriod.getDeliveryPeriod());
        project.setSummary(abstractForPdf);
        project.setLessonName(lessonType);
        project.setPdfAddress(filePath);
        project.setUserId(selectedUserId);

        transactions.addProject(project);
        int id = transactions.lastProjectId(); // son projenin id'sini donderir.
        addAuthors(authorList,id);
        addKeywords(keyWordList,id);
        addMentors(mentorList,id);

    }


    public void addAuthors(ArrayList<Author>authorList,int id){
        for(int i=0;i<authorList.size();i++){
            Author author = new Author(authorList.get(i).getName(),authorList.get(i).getSurname(),authorList.get(i).getStudentNo());
            transactions.addAuthor(author,id);
        }
    }

    public void addKeywords(String[] keyWordList, int id){
        for(int i=0;i<keyWordList.length;i++){
            transactions.addKeyword(keyWordList[i],id);
        }
    }

    public void addMentors(ArrayList<Mentor>mentorList,int id){
        for(int i=0;i<mentorList.size();i++){
            Mentor mentor = new Mentor(mentorList.get(i).getMentorType(),mentorList.get(i).getName(),mentorList.get(i).getSurname(),mentorList.get(i).getDegree());
            transactions.addMentor(mentor,id);
        }
    }

    public void writeKeyWordList(String[]keyWordList){
        for(String str:keyWordList){
            System.out.println(str);
        }
    }
    public void writeMentorInfo(ArrayList<Mentor> mentorList){
        for(Mentor m:mentorList){
            System.out.println(m.toString());
        }
    }
    public void writeAbstract(String abstractForPdf){
        System.out.println(abstractForPdf);
    }
    public void writeLessonType(String abstractForPdf){
        System.out.println(abstractForPdf);
    }
    public void writeAuthorInfo(ArrayList<Author> authorList){
        for(Author a:authorList){
            System.out.println(a.toString());
        }
    }
    public void writeProjectTitle(String projectTitle){
        System.out.println(projectTitle);
    }
    public void writeDeliveryPeriod(DeliveryPeriod deliveryPeriod){
        System.out.println("teslim y??l?? : "+deliveryPeriod.getDeliveryYear());
        System.out.println("teslim d??nemi : "+deliveryPeriod.getDeliveryPeriod());
    }
    public  String findProjectTitle(ArrayList<String> lineListLowerCase, ArrayList<String> lineList,String lessonType,ArrayList<Author> authorList) {
        findAuthorInfo(lineListLowerCase, lineList);
        String projectTitle = "";
        ArrayList<String> nameAuthorList = new ArrayList<>();
        for (Author s : authorList) {
            String nameSurname=s.getName()+s.getSurname();
            nameAuthorList.add(nameSurname);
        }
        int a = -1;
        for (int i = 0; i < lineListLowerCase.size(); i++) {
            String line = lineListLowerCase.get(i).trim();
            if (line.equals(lessonType)) {
                a = i;
                break;
            }
        }
        if (a != -1) {
            for (int j = a+1; j < a + 10; j++) {
                String line = lineListLowerCase.get(j).trim();
                //System.out.println(line);
                String controlLine=line.replaceAll(" ","");
                if (nameAuthorList.contains(controlLine)) {
                    break;
                }
                projectTitle += line +" ";
            }
        }
        return projectTitle;
    }
    public  String findLessonType(ArrayList<String> lineListLowerCase) {
        for (int i = 0; i < lineListLowerCase.size(); i++) {
            String line = lineListLowerCase.get(i).trim();
            if (line.equals("bilgisayar m??hendisli??i b??l??m??")) {
                for (int j = i; j < i + 10; j++) {
                    line = lineListLowerCase.get(j).trim();
                    if (line.equals("ara??t??rma problemleri")) {
                        return "ara??t??rma problemleri";
                    } else if (line.equals("bitirme projesi")) {
                        return "bitirme projesi";
                    }
                }
            }
        }
        return "";
    }


    public  String findAbstract(ArrayList<String> lineListLowerCase) {
        String abstractForPdf = "";
        for (int i = 0; i < lineListLowerCase.size(); i++) {
            String line = lineListLowerCase.get(i).trim();
            boolean isContinue = true;
            if (line.equals("??zet")) {
                String[] words = line.split(":");
                int j = i + 1;
                while (isContinue) {
                    line = lineListLowerCase.get(j).trim();
                    words = line.split(":");
                    if (words.length > 1) {
                        if (words[0].trim().equals("anahtar kelimeler")) {
                            isContinue = false;
                            break;
                        }
                    }
                    abstractForPdf += line + "\n";
                    j++;
                }

            }
            if (isContinue == false) {
                break;
            }
        }
        return abstractForPdf;
    }

    public  DeliveryPeriod findDeliveryPeriod(ArrayList<String> lineListLowerCase) {
        String deliveryPeriodStr="";
        DeliveryPeriod deliveryPeriod=new DeliveryPeriod();
        for (int i = 0; i < lineListLowerCase.size(); i++) {
            String[] wordsLowerCase = lineListLowerCase.get(i).trim().split(":");
            if (wordsLowerCase.length > 1) {
                wordsLowerCase[0] = wordsLowerCase[0].trim();
                if (wordsLowerCase[0].equals("tezin savunuldu??u tarih")) {
                    wordsLowerCase[1] = wordsLowerCase[1].trim();
                    String[] date = wordsLowerCase[1].split("[.|/-]");
                    int day = Integer.valueOf(date[0]);
                    int month = Integer.valueOf(date[1]);
                    int year = Integer.valueOf(date[2]);
                    //burada stream kullan??larak ge??erli ay??n listede ge??ip ge??memsi durumuna g??re d??nem ????kar??m????t??r
                    List<Integer> autumn = Arrays.asList(9, 10, 11, 12, 1, 2);
                    List<Integer> spring = Arrays.asList(3, 4, 5, 6, 7);
                    if (autumn.stream().anyMatch(n -> n == month)) {
                        //e??er g??z d??nemindeysek bulundu??umuz y??l ve sonraki y??l d??nemi belirler
                        deliveryPeriodStr= year + "-" + (year + 1) + " g??z";
                        deliveryPeriod.setDeliveryYear(year);
                        deliveryPeriod.setDeliveryPeriod(deliveryPeriodStr);
                    } else {
                        //e??er bahar d??nemindeysek bulundu??umuz y??l ve ??nceki y??l d??nem olur
                        deliveryPeriodStr= (year - 1) + "-" + (year) + " bahar";
                        deliveryPeriod.setDeliveryYear(year);
                        deliveryPeriod.setDeliveryPeriod(deliveryPeriodStr);
                    }
                }
            }
        }
        return deliveryPeriod;
    }

    public  ArrayList<Author> findAuthorInfo(ArrayList<String> lineListLowerCase, ArrayList<String> lineList) {
        String name = "";
        String surname = "";
        ArrayList<Author> AuthorList = new ArrayList<>();
        int authorNo = 0;
        if (lineListLowerCase != null && lineListLowerCase.size() > 0) {
            //sat??rlarda geziniyoruz
            for (int i = 0; i < lineListLowerCase.size(); i++) {
                String[] wordsLowerCase = lineListLowerCase.get(i).split(":");
                String[] words = lineList.get(i).split(":");
                if (wordsLowerCase.length > 1) {
                    //e??er yukar??daki gibi bir split meydana gelirse diz boyutumuz 1 den b??y??k olur
                    wordsLowerCase[0] = wordsLowerCase[0].trim();
                    words[0] = words[0].trim();
                    // sonra sorgulamalara ge??eriz
                    if (wordsLowerCase[0].equals("????renci no")) {
                        authorNo = Integer.valueOf(wordsLowerCase[1].trim());
                        //System.out.println(studentNo);
                    }
                    if (wordsLowerCase[0].equals("ad?? soyad??")) {
                        wordsLowerCase[1] = wordsLowerCase[1].trim();
                        words[1] = words[1].trim();
                        String[] nameSurname = findNameForAuthor(wordsLowerCase[1], words[1]);
                        name = nameSurname[0];
                        surname = nameSurname[1];
                    }
                }
                if (authorNo != 0 && !name.equals("") && !surname.equals("")) {
                    Author author = new Author(name.toLowerCase(), surname.toLowerCase(), authorNo);
                    //????renciyi olu??turduktan sonra de??erleri s??f??rlar??z
                    AuthorList.add(author);
                    authorNo = 0;
                    name = "";
                    surname = "";
                }

            }
        }
        return AuthorList;
    }

    public  String[] findNameForAuthor(String strLowerCase, String str) {
        String[] nameSurname = new String[2];
        nameSurname[0] = "";
        nameSurname[1] = "";
        String[] words = str.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].trim();
            if (!(Stream.of(" ", "").anyMatch(words[i]::equalsIgnoreCase))) {
                if (isName(words[i])) {
                    nameSurname[0] += words[i] + " ";
                } else {
                    nameSurname[1] += words[i] + " ";
                }
            }
        }
        nameSurname[0] = nameSurname[0].trim();
        nameSurname[1] = nameSurname[1].trim();
        return nameSurname;
    }

    public  ArrayList<Mentor> findMentorInfo(ArrayList<String> lineListLowerCase, ArrayList<String> lineList) {
        // burada dan????man ve ya j??ri bilgisi al??nm????t??r
        String name = "";
        String surname = "";
        String degree = "";
        String mentorType = "";
        ArrayList<Mentor> mentorList=new ArrayList<>();
        if (lineListLowerCase != null && lineListLowerCase.size() > 0) {
            for (int i = 0; i < lineListLowerCase.size(); i++) {
                String[] words = lineListLowerCase.get(i).split(",");
                if (words.length > 1) {
                    words[0] = words[0].trim();
                    if (words[0].equals("dan????man")) {
                        //??nceki sat??r??yollamal??y??z
                        String strLowerCase = lineListLowerCase.get(i - 1).trim();
                        String str = lineList.get(i - 1).trim();
                        String[] nameSurname = findNameForMentor(strLowerCase, str);
                        name = nameSurname[0];
                        surname = nameSurname[1];
                        mentorType = "dan????man";
                        degree = findDegree(strLowerCase);
                        Mentor mentor = new Mentor(mentorType,name.toLowerCase(), surname.toLowerCase(), degree.toLowerCase());
                        mentorList.add(mentor);
                    }
                    if (words[0].equals("j??ri ??yesi")) {
                        //??nceki sat??r??yollamal??y??z
                        String strLowerCase = lineListLowerCase.get(i - 1).trim();
                        String str = lineList.get(i - 1).trim();
                        String[] nameSurname = findNameForMentor(strLowerCase, str);
                        name = nameSurname[0];
                        surname = nameSurname[1];
                        mentorType = "j??ri";
                        degree = findDegree(strLowerCase);
                        Mentor mentor = new Mentor(mentorType,name.toLowerCase(), surname.toLowerCase(), degree.toLowerCase());
                        mentorList.add(mentor);
                    }
                }
            }
        }
        return mentorList;
    }

    public  String findDegree(String strLowerCase) {
        //ge??ebileek kelimelere g??re dan????manlar??n dereceleri d??nd??r??lm????t??r
        String[] wordsLowerCase = strLowerCase.split("[. ]");
        for (int i = 0; i < wordsLowerCase.length; i++) {
            wordsLowerCase[i] = wordsLowerCase[i].trim();
            if (Stream.of("prof", "profes??r").anyMatch(wordsLowerCase[i]::equalsIgnoreCase)) {
                return "profes??r";
            } else if (Stream.of("do??", "do??ent").anyMatch(wordsLowerCase[i]::equalsIgnoreCase)) {
                return "do??ent";
            } else if (Stream.of("????retim", "????retim ??yesi", "????r",
                    "doktor ????retim ??yesi").anyMatch(wordsLowerCase[i]::equalsIgnoreCase)) {
                return "doktor ????retim ??yesi";
            }
        }
        return "";
    }

    public  String[] findNameForMentor(String strLowerCase, String str) {
        //dan????man isimlerini alabilmek i??in
        String[] nameSurname = new String[2];
        nameSurname[0] = "";
        nameSurname[1] = "";
        str = str.trim();
        String[] degree = {"prof", "dr", "??yesi", "????r", "do??"};
        String[] words = str.split("[. ]");
        String[] wordsLoverCase = strLowerCase.split("[. ]");
        for (int i = 0; i < wordsLoverCase.length; i++) {
            if (!compareStringInArray(wordsLoverCase[i], degree)) {
                if (!(Stream.of(" ", "").anyMatch(wordsLoverCase[i]::equalsIgnoreCase))) {
                    words[i] = words[i].trim();
                    if (isName(words[i])) {
                        nameSurname[0] += words[i] + " ";
                    } else {
                        nameSurname[1] += words[i] + " ";
                    }
                }
            }
        }
        nameSurname[0] = nameSurname[0].trim();
        nameSurname[1] = nameSurname[1].trim();

        return nameSurname;
    }

    public  boolean isName(String str) {
        // burada gelen kelime e??er birden fazla b??y??k har i??eriyorsa soyad?? olarak de??erlendirilimi??tir
        int countUpperCase = 0;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                countUpperCase++;
                if (countUpperCase > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public  boolean compareStringInArray(String str, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (str.equals(arr[i])) {
                return true;
            }
        }
        return false;
    }

    public  String[] findKeyWords(ArrayList<String> lineList) {
        String keyWordsStr = "";
        if (lineList != null && lineList.size() > 0) {
            //sat??rlarda geziyoruz
            for (int i = 0; i < lineList.size(); i++) {
                String[] words = lineList.get(i).split(":");
                if (words.length > 1) {
                    //e??er yukar??daki splite g??re bir b??lme ger??ekle??ir ise kontrollere ba??l??yoruz
                    words[0] = words[0].trim();
                    if (words[0].equals("anahtar kelimeler")) {
                        keyWordsStr += words[1];
                        int j = i + 1;
                        //bo??lu??a denk gelene kadar keyWordStr mize ekleme yap??yoruz
                        while (!lineList.get(j).equals(" ")) {
                            keyWordsStr += lineList.get(j);
                            j++;
                        }
                        keyWordsStr = keyWordsStr.trim();
                    }
                }
            }
        }
        //burada , ve nokataya g??re ay??r??p arraye att??k vede bu ??ekilde anahtar kelime dizisi elde ettik
        String[] keyWordList = keyWordsStr.split("[.,]");
        for(int i=0;i<keyWordList.length;i++){
            keyWordList[i]=keyWordList[i].trim();
        }
        return keyWordList;
    }
    public void readPdf(String filePath){
        File file = new File(filePath);
        try (PDDocument document = PDDocument.load(file)) {
            document.getClass();
            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
                //System.out.println(pdfFileInText);

                String pdfFileInTextForName = pdfFileInText;

                pdfFileInText = pdfFileInText.toLowerCase(trlocale);

                String lines[] = pdfFileInText.split("\\r?\\n");

                String linesForName[] = pdfFileInTextForName.split("\\r?\\n");

                for (String line : lines) {
                    lineListLowerCase.add(line);
                }

                for (String line : linesForName) {
                    lineList.add(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
