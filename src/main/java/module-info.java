// Gerekli izinleri verdiğimiz yer
module com.erolkocoglu.ibb_ecodation_javafx {

    // JavaFX'in temel bileşenlerini kullanmak için gerekli modüller
    // JavaFX kontrol bileşenlerini (Button, Label, TextField vb.) kullanabilmek için gereklidir.
    requires javafx.controls;

    // JavaFX FXML dosyalarını (FXML UI tasarımları) yükleyebilmek için gereklidir.
    requires javafx.fxml;

    // WEB
    //requires javafx.web;

    // #######################################################################################
    // UI geliştirme için kullanılan harici kütüphaneler
    // ControlsFX, gelişmiş UI bileşenlerini (örn: Notifikasyonlar, Doğrulama Alanları) sağlar.
    requires org.controlsfx.controls;

    // FormsFX, formlar için gelişmiş bileşenler sunan bir kütüphanedir.
    requires com.dlsc.formsfx;

    // ValidatorFX, form doğrulama işlemleri için kullanılır.
    requires net.synedra.validatorfx;

    // İkon kütüphanesi, UI'de çeşitli ikonları kullanmaya olanak tanır.
    requires org.kordamp.ikonli.javafx;

    // BootstrapFX, Bootstrap benzeri CSS stillerini JavaFX'e entegre eder.
    requires org.kordamp.bootstrapfx.core;

    // #############################################
    // Lombok kütüphanesi, Java'da getter, setter, constructor gibi metotları otomatik oluşturur.
    // Lombok, derleme zamanı (compile-time) kullanıldığı için "static" olarak eklenmiştir.
    requires static lombok;

    // JDBC ile veritabanı bağlantısı kurabilmek için gerekli modül
    // Java'daki SQL işlemlerini (Connection, Statement, ResultSet vb.) gerçekleştirebilmek için gereklidir.
    requires com.h2database;
    requires jbcrypt;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires java.mail;
    requires com.google.gson;
    requires java.sql;
    //requires eu.hansolo.tilesfx;
    //opens com.erolkocoglu.ibb_ecodation_javafx.dto to com.google.gson;
    // #######################################################################################
    // Paket Erişimlerine İzin vermek
    // `opens` ifadesi, bir paketin runtime'da (çalışma zamanında) refleksiyon (reflection) kullanılarak erişilebilir olmasını sağlar.
    // Ana paket (Root package) açılıyor, böylece FXML dosyalarından erişilebilir.
    opens com.erolkocoglu.ibb_ecodation_javafx to javafx.fxml;

    // DTO (Data Transfer Object) paketinin içeriği, JavaFX bileşenleri ve Lombok tarafından erişilebilir olmalıdır.
    opens com.erolkocoglu.ibb_ecodation_javafx.dto to javafx.base, lombok, com.google.gson;

    // Controller sınıfları FXML tarafından kullanılacağı için açılması gerekiyor.
    opens com.erolkocoglu.ibb_ecodation_javafx.controller to javafx.fxml;

    // DAO (Data Access Object) sınıfları, SQL bağlantısı kullandığı için açılıyor.
    opens com.erolkocoglu.ibb_ecodation_javafx.dao to java.sql;

    // Veritabanı bağlantısı sağlayan sınıfların da SQL modülüne açık olması gerekiyor.
    opens com.erolkocoglu.ibb_ecodation_javafx.database to java.sql;

    // #####################################################################
    // Paket dışa aktarmak
    // `exports` ifadesi, paketin diğer modüller tarafından erişilebilir olmasını sağlar.

    // DAO sınıflarını dışarıya açıyoruz. Böylece başka modüller veritabanı işlemlerini çağırabilir.
    exports com.erolkocoglu.ibb_ecodation_javafx.dao;

    // // Veritabanı bağlantı paketini dış dünyaya açıyoruz. Diğer modüller DB bağlantısını kullanabilir.
    exports com.erolkocoglu.ibb_ecodation_javafx.database;

    // Ana paketi dış dünyaya açıyoruz. Diğer modüller bu paketin içeriğini kullanabilir.
    exports com.erolkocoglu.ibb_ecodation_javafx;
    opens com.erolkocoglu.ibb_ecodation_javafx.utils to javafx.base, lombok, com.google.gson;
}

//Default
/*
module com.erolkocoglu.ibb_ecodation_javafx {
        requires javafx.controls;
        requires javafx.fxml;
        //requires javafx.web;

        requires org.controlsfx.controls;
        requires com.dlsc.formsfx;
        requires net.synedra.validatorfx;
        requires org.kordamp.ikonli.javafx;
        requires org.kordamp.bootstrapfx.core;
        requires static lombok;
        requires java.sql;
        requires java.desktop;
        //requires eu.hansolo.tilesfx;

        opens com.hamitmizrak.ibb_ecodation_javafx to javafx.fxml;
        exports com.hamitmizrak.ibb_ecodation_javafx;
        }
*/