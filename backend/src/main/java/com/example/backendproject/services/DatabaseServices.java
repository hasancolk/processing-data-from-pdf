package com.example.backendproject.services;

import com.example.backendproject.database.Database;
import com.example.backendproject.entity.*;
import com.example.backendproject.program.Transactions;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DatabaseServices {
    private Connection con = null;

    private Statement statement = null;

    private PreparedStatement preparedStatement = null;
    private PreparedStatement preparedStatement2 = null;

    public DatabaseServices() {

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

    public ArrayList<Author> callGetAuthor(int selectedUserId) {
        if (selectedUserId == -10) {
            ArrayList<Author> allAuthor = new ArrayList<>();
            ArrayList<User> userList = getUsers();
            for (int i = 0; i < userList.size(); i++) {
                ArrayList<Author> alist = getAuthor(userList.get(i).getId());
                for (int j = 0; j < alist.size(); j++) {
                    allAuthor.add(alist.get(j));
                }
            }
            return allAuthor;
        } else {
            return getAuthor(selectedUserId);
        }
    }

    public ArrayList<Author> getAuthor(int selectedUserId) {
        ArrayList<Author> authors = new ArrayList<>();
        String query = "SELECT y.id ,y.ad ,y.soyad ,y.ogrenciNo,y.ogretimTur" +
                " FROM yazar AS y , proje_yazarlari AS py , proje AS p" +
                " WHERE py.projeId=p.id AND y.id=py.yazarId AND p.kullaniciId=?;";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, selectedUserId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("y.id");
                String name = rs.getString("y.ad");
                String surname = rs.getString("y.soyad");
                int studentNo = rs.getInt("y.ogrenciNo");
                int typeEdu = rs.getInt("y.ogretimTur");
                authors.add(new Author(id, name, surname, studentNo, typeEdu));
            }
            return authors;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getLessonType() {
        ArrayList<String> lessons = new ArrayList<>();
        lessons.add("bitirme projesi");
        lessons.add("araştırma problemleri");
        return lessons;
    }

    public ArrayList<Project> callGetProjects(int selectedUserId) {
        if (selectedUserId == -10) {
            ArrayList<Project> allProject = new ArrayList<>();
            ArrayList<User> userList = getUsers();
            for (int i = 0; i < userList.size(); i++) {
                ArrayList<Project> plist = getProjects(userList.get(i).getId());
                for (int j = 0; j < plist.size(); j++) {
                    allProject.add(plist.get(j));
                }
            }
            return allProject;
        } else {
            return getProjects(selectedUserId);
        }
    }

    public ArrayList<Project> getProjects(int selectedUserId) {
        ArrayList<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM proje WHERE kullaniciId=?;";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, selectedUserId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("kullaniciId");
                String name = rs.getString("ad");
                String deliveryPeriod = rs.getString("teslimDonemi");
                String summary = rs.getString("ozet");
                String lessonName = rs.getString("dersAd");
                String pdfAddress = rs.getString("pdfAdres");
                projects.add(new Project(id, userId, name, deliveryPeriod, summary, lessonName, pdfAddress));
            }
            return projects;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Keyword> callGetKeyword(int selectedUserId) {
        if (selectedUserId == -10) {
            ArrayList<Keyword> allKeyword = new ArrayList<>();
            ArrayList<User> users = getUsers();
            for (int i = 0; i < users.size(); i++) {
                ArrayList<Keyword> klist = getKeywords(users.get(i).getId());
                for (int j = 0; j < klist.size(); j++) {
                    allKeyword.add(klist.get(j));
                }
            }
            return allKeyword;
        } else {
            return getKeywords(selectedUserId);
        }
    }

    public ArrayList<Keyword> getKeywords(int selectedUserId) {
        ArrayList<Keyword> keywords = new ArrayList<>();
        String query = "SELECT word.id ,word.kelime " + " " +
                "FROM proje AS p, proje_anahtar_kelime AS pword , anahtar_kelime AS word " +
                " WHERE p.id=pword.projeId AND word.id=pword.anahtarKelimeId AND p.kullaniciId=?;";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, selectedUserId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String word = rs.getString("kelime");
                keywords.add(new Keyword(id, word));
            }
            return keywords;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getDeliveryPeriod() {
        ArrayList<String> deliveryPeriodList = new ArrayList<>();
        String query = "SELECT * FROM proje";
        try {
            preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String deliveryPeriod = rs.getString("teslimDonemi");
                if (deliveryPeriodList.size() > 0) {
                    if (!deliveryPeriodList.contains(deliveryPeriod)) {
                        deliveryPeriodList.add(deliveryPeriod);
                    }
                } else {
                    deliveryPeriodList.add(deliveryPeriod);
                }
            }
            return deliveryPeriodList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Project> callGetProjectForAuthor(String nameSurnameStr, int selectedUserId) {
        if (selectedUserId == -10) {
            ArrayList<Project> allProjects = new ArrayList<>();
            ArrayList<User> userList = getUsers();
            for (int i = 0; i < userList.size(); i++) {
                ArrayList<Project> plist = getProjectsForAuthor(nameSurnameStr, userList.get(i).getId());
                for (int j = 0; j < plist.size(); j++) {
                    allProjects.add(plist.get(j));
                }
            }
            return allProjects;
        } else {
            return getProjectsForAuthor(nameSurnameStr, selectedUserId);
        }
    }

    public ArrayList<Project> getProjectsForAuthor(String nameSurnameStr, int selectedUserId) {
        //yazarlar için proje listesini dönderir
        ArrayList<Project> projects = new ArrayList<>();
        String[] nameSurname = nameSurnameStr.trim().split(" ");
        String queryName = nameSurname[0].trim();
        String querySurname = nameSurname[1].trim();
        String attributesSelect = " SELECT p.id,p.kullaniciid,p.ad,p.teslimdonemi,p.ozet,p.dersad,p.pdfadres ";
        String tableUse = " FROM proje_yazarlari as py,proje as p,yazar as y ,kullanici as k ";
        String condition = " WHERE y.id=py.yazarid AND p.id=py.projeid AND k.id=p.kullaniciid AND k.id=? AND y.ad=? AND y.soyad=?";
        String query = attributesSelect + tableUse + condition;
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, selectedUserId);
            preparedStatement.setString(2, queryName);
            preparedStatement.setString(3, querySurname);
            ResultSet rs = preparedStatement.executeQuery();
            return returnResultSetForProject(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Project> callGetProjectForLesson(String lesson, int selectedUserId) {
        if (selectedUserId == -10) {
            ArrayList<Project> allProjects = new ArrayList<>();
            ArrayList<User> userList = getUsers();
            for (int i = 0; i < userList.size(); i++) {
                ArrayList<Project> plist = getProjectForLesson(lesson, userList.get(i).getId());
                for (int j = 0; j < plist.size(); j++) {
                    allProjects.add(plist.get(j));
                }
            }
            return allProjects;
        } else {
            return getProjectForLesson(lesson, selectedUserId);
        }
    }

    public ArrayList<Project> getProjectForLesson(String lesson, int selectedUserId) {
        //ders tipine göre proje listesi dönderir
        ArrayList<Project> projects = new ArrayList<>();
        String attributesSelect = " SELECT DISTINCT p.id,p.kullaniciid, p.ad,p.teslimdonemi,p.ozet,p.dersad,p.pdfadres ";
        String tableUse = " FROM proje_yazarlari as py,proje as p,yazar as y ,kullanici as k ";
        String condition = " WHERE y.id=py.yazarid AND p.id=py.projeid AND k.id=p.kullaniciid AND k.id=? and p.dersAd=?";
        String query = attributesSelect + tableUse + condition;
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, selectedUserId);
            preparedStatement.setString(2, lesson);
            ResultSet rs = preparedStatement.executeQuery();
            return returnResultSetForProject(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Project> callGetProjectForDeliveryPeriod(String deliveryPeriod, int selectedUserId) {
        if (selectedUserId == -10) {
            ArrayList<Project> allProjects = new ArrayList<>();
            ArrayList<User> userList = getUsers();
            for (int i = 0; i < userList.size(); i++) {
                ArrayList<Project> plist = getProjectForDeliveryPeriod(deliveryPeriod, userList.get(i).getId());
                for (int j = 0; j < plist.size(); j++) {
                    allProjects.add(plist.get(j));
                }
            }
            return allProjects;
        } else {
            return getProjectForDeliveryPeriod(deliveryPeriod, selectedUserId);
        }
    }

    public ArrayList<Project> getProjectForDeliveryPeriod(String deliveryPeriod, int selectedUserId) {
        // teslim dönemine göre proje listesi dönderir
        ArrayList<Project> projects = new ArrayList<>();
        String attributesSelect = " SELECT DISTINCT p.id,p.kullaniciid, p.ad,p.teslimdonemi,p.ozet,p.dersad,p.pdfadres ";
        String tableUse = " FROM proje_yazarlari as py,proje as p,yazar as y ,kullanici as k ";
        String condition = " WHERE y.id=py.yazarid AND p.id=py.projeid AND k.id=p.kullaniciid AND k.id=? AND p.teslimdonemi=?";
        String query = attributesSelect + tableUse + condition;
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, selectedUserId);
            preparedStatement.setString(2, deliveryPeriod);
            ResultSet rs = preparedStatement.executeQuery();
            return returnResultSetForProject(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Project> callGetProjectForKeywords(String keyWord, int selectedUserId) {
        if (selectedUserId == -10) {
            ArrayList<Project> allProjects = new ArrayList<>();
            ArrayList<User> userList = getUsers();
            for (int i = 0; i < userList.size(); i++) {
                ArrayList<Project> plist = getProjectForKeyWords(keyWord, userList.get(i).getId());
                for (int j = 0; j < plist.size(); j++) {
                    allProjects.add(plist.get(j));
                }
            }
            return allProjects;
        } else {
            return getProjectForKeyWords(keyWord, selectedUserId);
        }
    }

    public ArrayList<Project> getProjectForKeyWords(String keyWord, int selectedUserId) {
        //anahtar kelimelere göre proje listesi dönderir
        ArrayList<Project> projects = new ArrayList<>();
        String attributesSelect = " SELECT  DISTINCT p.id,p.kullaniciid, p.ad,p.teslimdonemi,p.ozet,p.dersad,p.pdfadres  ";
        String tableUse = " FROM proje as p,kullanici as k ,proje_anahtar_kelime as pkey ,anahtar_kelime as keyw ";
        String condition = " WHERE pkey.projeId=p.id AND pkey.anahtarKelimeId=keyw.id AND p.kullaniciid=k.id and k.id=? AND keyw.kelime=?";
        String query = attributesSelect + tableUse + condition;
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, selectedUserId);
            preparedStatement.setString(2, keyWord);
            ResultSet rs = preparedStatement.executeQuery();
            return returnResultSetForProject(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Author> callGetAuthorForProjectName(String projectName, int selectedUserId) {
        if (selectedUserId == -10) {
            ArrayList<Author> allAuthor = new ArrayList<>();
            ArrayList<User> userList = getUsers();
            for (int i = 0; i < userList.size(); i++) {
                ArrayList<Author> alist = getAuthorForProjectName(projectName, userList.get(i).getId());
                for (int j = 0; j < alist.size(); j++) {
                    allAuthor.add(alist.get(j));
                }
            }
            return allAuthor;
        } else {
            return getAuthorForProjectName(projectName, selectedUserId);
        }
    }

    public ArrayList<Author> getAuthorForProjectName(String projectName, int selectedUserId) {
        //proje adına göre yazar listesini dönderir
        ArrayList<Project> projects = new ArrayList<>();
        String attributesSelect = " SELECT y.id , y.ad ,y.soyad ,y.ogrenciNo ,y.ogretimTur  ";
        String tableUse = " FROM proje AS p ,yazar AS y, proje_yazarlari AS py , kullanici AS k  ";
        String condition = " WHERE p.id=py.projeId AND py.yazarId=y.id AND k.id=p.kullaniciId AND p.ad=? AND k.id=? ";
        String query = attributesSelect + tableUse + condition;
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, projectName);
            preparedStatement.setInt(2, selectedUserId);
            ResultSet rs = preparedStatement.executeQuery();
            return returnResultSetForAuthor(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Project> getProjectForPeriodAndForLesson(String deliveryPeriod, String userName,String lessonType) {
        ArrayList<Project> plist = new ArrayList<>();
        String query = "SELECT p.id ,p.kullaniciId,p.ad,p.teslimDonemi,p.ozet,p.dersAd,p.pdfAdres "+
                " FROM kullanici AS k , proje AS p  "+" " +
                "WHERE p.kullaniciId=k.id AND p.teslimDonemi=? AND k.kullaniciAd=? AND p.dersAd=?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,deliveryPeriod);
            preparedStatement.setString(2,userName);
            preparedStatement.setString(3,lessonType);
            ResultSet rs = preparedStatement.executeQuery();
            plist = returnResultSetForProject(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plist;
    }

    public ArrayList<Author> returnResultSetForAuthor(ResultSet rs) throws SQLException {
        ArrayList<Author> authors = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("ad");
            String surname = rs.getString("soyad");
            int studentNo = rs.getInt("ogrenciNo");
            int typeEdu = rs.getInt("ogretimTur");
            authors.add(new Author(id, name, surname, studentNo, typeEdu));
        }
        return authors;
    }

    public ArrayList<Project> returnResultSetForProject(ResultSet rs) throws SQLException {
        ArrayList<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("p.id");
            int userId = rs.getInt("p.kullaniciId");
            String name = rs.getString("p.ad");
            String deliveryPeriod = rs.getString("p.teslimDonemi");
            String summary = rs.getString("p.ozet");
            String lessonName = rs.getString("p.dersAd");
            String pdfAddress = rs.getString("p.pdfAdres");
            projects.add(new Project(id, userId, name, deliveryPeriod, summary, lessonName, pdfAddress));
        }
        return projects;
    }

    public int userLogin(String username, String password) {
        String sorgu = "select * from kullanici where kullaniciAd=? and sifre=?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public boolean adminLogin(String ad, String sifre) {
        String sorgu = "select * from admin where ad=? and sifre=?";
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, sifre);

            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public ArrayList<User> getUsers() {
        String sorgu = "SELECT * FROM `kullanici` WHERE 1";
        ArrayList<User> result = new ArrayList<>();
        try {
            preparedStatement = con.prepareStatement(sorgu);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("kullaniciAd");
                String password = rs.getString("sifre");

                result.add(new User(id, name, password));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int signControl(String username) {
        String sorgu = "SELECT * FROM kullanici WHERE kullaniciAd=?";
        int sonuc = 0;
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                sonuc = rs.getInt("id");
            }
            return sonuc;
        } catch (SQLException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("hata verdi");
            return -1;
        }
    }

    public int signUser(String username, String password) {
        int var = signControl(username);

        if (var == 0) {
            String sorgu = "INSERT INTO `kullanici`( `kullaniciAd`, `sifre`) VALUES (?,?)";
            try {
                preparedStatement = con.prepareStatement(sorgu);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
                return 1;
            } catch (SQLException ex) {
                Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }

        } else {
            //varsa
            return 0;
        }

    }
    public int  deleteUser(String userName){
        String query="DELETE FROM kullanici WHERE kullanici.id=(SELECT k.id FROM kullanici AS k WHERE k.kullaniciAd=?)";
        try {
            preparedStatement= con.prepareStatement(query);
            preparedStatement.setString(1,userName);
            preparedStatement.executeUpdate();
            //silinirse
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            //silinmezse
            return -1;
        }
    }
    public int updateUser(String newUserName , String newPassword ,String userName){
        String query="UPDATE kullanici AS üstk "+"" +
                "SET üstk.kullaniciAd=?, üstk.sifre=? "+" " +
                "WHERE üstk.id = (SELECT k.id FROM kullanici AS k WHERE k.kullaniciAd=?) "+
                "  AND ? NOT IN(SELECT kullaniciAd FROM kullanici WHERE id!=üstk.id);";
        try {
            preparedStatement=con.prepareStatement(query);
            preparedStatement.setString(1,newUserName);
            preparedStatement.setString(2,newPassword);
            preparedStatement.setString(3,userName);
            preparedStatement.setString(4,newUserName);
            preparedStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public ProjectFullData returnProjectFullData(int projectId) {
        String userName="";
        String projectName="";
        String deliveryPeriod="";
        String abstractForPdf="";
        String lessonType="";
        String query = "SELECT k.kullaniciAd ,p.ad,p.teslimDonemi ,p.ozet ,p.dersAd " + " " +
                "FROM kullanici as k ,proje AS p " +
                " WHERE k.id=p.kullaniciId AND p.id=?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, projectId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                userName = rs.getString("k.kullaniciAd");
                projectName = rs.getString("p.ad");
                deliveryPeriod = rs.getString("p.teslimDonemi");
                abstractForPdf = rs.getString("p.ozet");
                lessonType = rs.getString("p.dersAd");
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Mentor> mentors = findMentorForProjectId(projectId);
        ArrayList<Author> authors = findAuthorForProjectId(projectId);
        ProjectFullData data = new ProjectFullData(userName,projectName,deliveryPeriod,abstractForPdf,lessonType,
                authors,mentors);
        if(!userName.equals("")){
            return data;
        }else {
            return null;
        }
    }

    public ArrayList<Author> findAuthorForProjectId(int projectId) {
        ArrayList<Author> authors = new ArrayList<>();
        String query = "SELECT y.id , y.ad ,y.soyad ,y.ogrenciNo ,y.ogretimTur " +
                " FROM proje_yazarlari AS py , proje AS p ,yazar AS y " +
                " WHERE py.projeId=p.id AND y.id=py.yazarId AND p.id=?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, projectId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("y.id");
                String name = rs.getString("y.ad");
                String surname = rs.getString("y.soyad");
                int studentNo = rs.getInt("y.ogrenciNo");
                int typeEdu = rs.getInt("y.ogretimTur");
                authors.add(new Author(id, name, surname, studentNo, typeEdu));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public ArrayList<Mentor> findMentorForProjectId(int projectId) {
        ArrayList<Mentor> mentors = new ArrayList<>();
        String query = "SELECT m.ad,m.soyad,m.unvan,pm.mentorTur " +
                " FROM mentor AS m ,proje_mentorleri AS pm ,proje AS p " +
                " WHERE p.id=pm.projeId AND pm.mentorId=m.id AND p.id=?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, projectId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String mentorType = rs.getString("pm.mentorTur");
                String name = rs.getString("m.ad");
                String surname = rs.getString("m.soyad");
                String degree = rs.getString("m.unvan");
                mentors.add(new Mentor(mentorType, name, surname, degree));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mentors;
    }

}
