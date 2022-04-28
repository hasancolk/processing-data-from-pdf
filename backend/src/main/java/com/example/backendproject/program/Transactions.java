package com.example.backendproject.program;

import com.example.backendproject.database.Database;
import com.example.backendproject.entity.Author;
import com.example.backendproject.entity.Mentor;
import com.example.backendproject.entity.Project;
import com.example.backendproject.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Transactions {

    private Connection con = null;

    private Statement statement = null;

    private PreparedStatement preparedStatement = null;
    private PreparedStatement preparedStatement2 = null;

    public Transactions(){

        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db_ismi + "?useUnicode=true&characterEncoding=utf8";
        //"?useUnicode=true&characterEncoding=utf8"
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("driver bulunamadi...");
        }

        try {
            con = DriverManager.getConnection(url, Database.kullanici_adi, Database.parola);
            System.out.println("Baglanti başarılı");
        } catch (SQLException ex) {
            System.out.println("Baglanti Başarısız...");
            //ex.printStackTrace();
        }

    }



    public void addAuthor(Author author,int projectId) { // yazar eklenir.

        int yazarId = findAuthorId(author.getStudentNo()); // eger 0 ise yazar daha once sisteme eklenmemiştir.

        if(yazarId==0){ // yazar daha önceden sisteme eklenmediyse "yazar" tablosuna eklenir.
            String sorgu1 = "INSERT INTO `yazar`( `ad`, `soyad`, `ogrenciNo`, `ogretimTur`) VALUES (?,?,?,?)";
            try {
                preparedStatement = con.prepareStatement(sorgu1);
                preparedStatement.setString(1, author.getName());
                preparedStatement.setString(2, author.getSurname());
                preparedStatement.setInt(3, author.getStudentNo());
                preparedStatement.setInt(4, author.getTypeEdu());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            }
            yazarId = findAuthorId(author.getStudentNo());
        }

        String sorgu2 = "INSERT INTO `proje_yazarlari`( `projeId`, `yazarId`) VALUES (?,?)"; // proje ile yazar eşleştirilir.
        try {
            preparedStatement = con.prepareStatement(sorgu2);
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, yazarId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int findAuthorId(int studentNo){ // numarasi verilen yazarın id'sini donderir.
        String sorgu = "SELECT * FROM yazar WHERE ogrenciNo=?";
        int yazarId = 0; // yazarId eger 0 ise yazar daha önce sisteme eklenmemiştir.
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setInt(1, studentNo);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                yazarId = rs.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("hata verdi");
        }
        return yazarId;
    }

    public void addMentor(Mentor mentor, int projectId) { // mentor eklenir.

        int mentorId = findMentorId(mentor); // eger 0 ise mentor daha once sisteme eklenmemiştir.

        if(mentorId==0){ // mentor daha önceden sisteme eklenmediyse "mentor" tablosuna eklenir.
            String sorgu1 = "INSERT INTO `mentor`( `ad`, `soyad`, `unvan`) VALUES (?,?,?)";
            try {
                preparedStatement = con.prepareStatement(sorgu1);
                preparedStatement.setString(1, mentor.getName());
                preparedStatement.setString(2, mentor.getSurname());
                preparedStatement.setString(3, mentor.getDegree());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            }
            mentorId = findMentorId(mentor);
        }

        String sorgu2 = "INSERT INTO `proje_mentorleri`( `projeId`, `mentorId`, `mentorTur`) VALUES (?,?,?)"; // proje ile mentor eşleştirilir.
        try {
            preparedStatement = con.prepareStatement(sorgu2);
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, mentorId);
            preparedStatement.setString(3, mentor.getMentorType());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int findMentorId(Mentor mentor){ // numarasi verilen mentorun id'sini donderir.
        String sorgu = "SELECT * FROM mentor WHERE ad=?  and soyad=? and unvan=?";
        int mentorId = 0; // mentorId eger 0 ise mentor daha önce sisteme eklenmemiştir.
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, mentor.getName());
            preparedStatement.setString(2, mentor.getSurname());
            preparedStatement.setString(3, mentor.getDegree());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                mentorId = rs.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("hata verdi");
        }
        return mentorId;
    }

    public ArrayList<Author> getAuthor() { // tum yazarlari getirir.
        String sorgu = "SELECT * FROM `yazar` WHERE 1";
        ArrayList<Author> result = new ArrayList<>();
        try {
            preparedStatement = con.prepareStatement(sorgu);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("ad");
                String surname = rs.getString("soyad");
                int studentNo = rs.getInt("ogrenciNo");
                int typeEdu = rs.getInt("ogretimTur");

                result.add(new Author(id, name, surname, studentNo,typeEdu));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void addKeyword(String keyword,int projectId) { // anahtar kelime eklenir.

        int keywordId = findKeywordId(keyword); // eger 0 ise kelime daha once sisteme eklenmemiştir.

        if(keywordId==0){ // kelime daha önceden sisteme eklenmediyse "kelime" tablosuna eklenir.
            String sorgu1 = "INSERT INTO `anahtar_kelime`( `kelime`) VALUES (?)";
            try {
                preparedStatement = con.prepareStatement(sorgu1);
                preparedStatement.setString(1, keyword);
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            }
            keywordId = findKeywordId(keyword);
        }

        String sorgu2 = "INSERT INTO `proje_anahtar_kelime`( `projeId`, `anahtarKelimeId`) VALUES (?,?)"; // anahtar kelime ile proje eşleştirilir.
        try {
            preparedStatement = con.prepareStatement(sorgu2);
            preparedStatement.setInt(1, projectId);
            preparedStatement.setInt(2, keywordId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int findKeywordId(String keyword){ // numarasi verilen yazarın id'sini donderir.
        String sorgu = "SELECT * FROM anahtar_kelime WHERE kelime=?";
        int keywordId = 0; // keywordId eger 0 ise kelime daha önce sisteme eklenmemiştir.
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, keyword);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                keywordId = rs.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("hata verdi");
        }
        return keywordId;
    }

    public void addProject(Project project) {

        String sorgu = "INSERT INTO `proje`( `kullaniciId`, `ad`, `teslimDonemi`, `ozet`, `dersAd`, `pdfAdres`) VALUES (?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setInt(1, project.getUserId());
            preparedStatement.setString(2, project.getName());
            preparedStatement.setString(3, project.getDeliveryPeriod());
            preparedStatement.setString(4, project.getSummary());
            preparedStatement.setString(5, project.getLessonName());
            preparedStatement.setString(6, project.getPdfAddress());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Project> getProjects(){

        ArrayList<Project> projects = new ArrayList<Project>();

        try {
            statement =  con.createStatement();
            String sorgu =  "select * From proje";

            ResultSet rs =  statement.executeQuery(sorgu);

            while(rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("kullaniciId");
                String name = rs.getString("ad");
                String deliveryPeriod = rs.getString("teslimDonemi");
                String summary = rs.getString("ozet");
                String lessonName = rs.getString("dersAd");
                String pdfAddress = rs.getString("pdfAdres");

                projects.add(new Project(id,userId,name,deliveryPeriod,summary,lessonName,pdfAddress));

            }
            return projects;

        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            return null;

        }
    }

    public int lastProjectId() {
        ArrayList<Project> projects = getProjects();
        int id = projects.get(projects.size()-1).getId();
        return id;
    }




}
