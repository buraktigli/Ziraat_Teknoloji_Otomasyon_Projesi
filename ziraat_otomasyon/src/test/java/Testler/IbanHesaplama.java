package Testler;

import org.junit.jupiter.api.AfterEach; //AfterEach annotation kullanimi icin "junit-jupiter-api" isimli Maven dependency pom.xml'e eklendi
import org.junit.jupiter.api.BeforeEach; //BeforeEach annotation kullanimi icin "junit-jupiter-api" isimli Maven dependency pom.xml'e eklendi
import org.junit.jupiter.api.Test; //Test annotation kullanimi icin "junit-jupiter-api" isimli Maven dependency pom.xml'e eklendi
//import dev.failsafe.internal.util.Assert;
import org.junit.jupiter.api.Assertions; //Assortions kullanımı için "junit-jupiter-api" isimli Maven dependency pom.xml'e eklendi
import org.openqa.selenium.*; // WebDriver kullanmak icin
import org.openqa.selenium.chrome.ChromeDriver; // chrome driver kullanabilmek icin "selenium-chrome-driver" isimli Maven dependency pom.xml'e eklendi
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait; // WebDriverWait kullanabilmek icin "selenium-support" isimli Maven dependency pom.xml'e eklendi.
import org.openqa.selenium.support.ui.ExpectedConditions; // ExpectedConditions kullanabilmek icin "selenium-support" isimli Maven dependency pom.xml'e eklendi.
//import java.io.IOException;
import java.time.Duration; //dinamic wait methodunu kullanabilmek için eklendi.
import java.util.Random; //random sayı oluşturmak için eklendi.

public class IbanHesaplama {

    WebDriver driver;

    @BeforeEach
    void setup(){
        driver = new ChromeDriver();


        driver.manage().window().maximize(); //açılan chrome sayfasını tam ekran boyutu yapmak için kullanıldı.
        driver.get("https://www.ziraatbank.com.tr/tr/hesaplama-araclari"); //ziraatbank.com sitesine git
    }

    @Test
    void ibanHesaplama_ismeGoreSirala_PassedTest() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("ibanHesaplama_ismeGoreSirala_PassedTest testi basladi.");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='landingNav']//a[.='IBAN Hesaplama']"))); //iban hesaplama butonunu görene kadar bekle
        driver.findElement(By.xpath("//div[@id='landingNav']//a[.='IBAN Hesaplama']")).click(); //iban hesaplama butonuna tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[5]/a[.='IBAN Hesaplama']"))); //iban hesaplama sayfasındaki path'i görene kadar bekle

        String ibanHesaplamaPathKontrol = driver.findElement(By.xpath("//span[5]/a[.='IBAN Hesaplama']")).getText(); //iban Hesaplama ekranındaki path'i kaydet
        Assertions.assertEquals("IBAN Hesaplama",ibanHesaplamaPathKontrol);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("\tiban hesaplama sayfasinin acildigi dogrulandi ->> " + ibanHesaplamaPathKontrol);

        driver.findElement(By.xpath("//label[.='Şubeleri İsimlerine Göre Sırala']")).click(); // isme göre sırala seç
        System.out.println("Şubeleri İsimlerine Göre Sırala seçeneği seçildi.");

        WebElement ismeGoreSecimi = driver.findElement(By.xpath("//div[@class='radio-box sort-name']//span[1]"));
        Assertions.assertEquals(driver.findElement(By.xpath("//span[@class='active']")), ismeGoreSecimi);
        System.out.println("\tsube ismine gore sirala seciminin yapildigi goruldu");

        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //şube seçimi için tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='select2-search__field']"))); //şube girişi yapılacak alan açılana kadar bekle
        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys("abana"); //abana için seçim yap
        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='musteriNo']")));
        driver.findElement(By.xpath("//input[@id='musteriNo']")).click(); //müşteri no için tıkla


        //8 ya da 9 haneli random sayi üretme
        int min1 = 10000000;
        int max1 = 999999999;
        Random rand1 = new Random();
        int intMusteriNo = rand1.nextInt(max1-min1) + min1;
        String musteriNo = Integer.toString(intMusteriNo);

        driver.findElement(By.xpath("//input[@id='musteriNo']")).sendKeys(musteriNo);
        System.out.println("random musteri no girildi: " + musteriNo);

        driver.findElement(By.xpath("//input[@id='ekNo']")).click(); //ek no alanına tıkla

        //4 haneli random sayı üretme
        int min2 = 1000;
        int max2 = 9999;
        Random rand2 = new Random();
        int intEkNo = rand2.nextInt(max2-min2) + min2;
        String ekNo = Integer.toString(intEkNo);

        driver.findElement(By.xpath("//input[@id='ekNo']")).sendKeys(ekNo); // ek no gir
        System.out.println("random ek no girildi: " + ekNo);
        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[.='IBAN']"))); // iban yazısını görene kadar bekle

        String ekrandakiIbanYazisi = driver.findElement(By.cssSelector(".result-item > label")).getText();
        Assertions.assertEquals("IBAN", ekrandakiIbanYazisi);
        System.out.println("\tiban olusturuldugu dogrulandi ->> " + ekrandakiIbanYazisi);
        String iban = driver.findElement(By.cssSelector(".result-item > span")).getText();
        System.out.println("iban: " + iban);
        System.out.println("-------------------------------------------");
    }

    @Test
    void ibanHesaplama_ismeGoreSirala_FailedTest() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("ibanHesaplama_ismeGoreSirala_FailedTest_testi basladi.");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='landingNav']//a[.='IBAN Hesaplama']"))); //iban hesaplama butonunu görene kadar bekle
        driver.findElement(By.xpath("//div[@id='landingNav']//a[.='IBAN Hesaplama']")).click(); //iban hesaplama butonuna tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[5]/a[.='IBAN Hesaplama']"))); //iban hesaplama sayfasındaki path'i görene kadar bekle

        String ibanHesaplamaPathKontrol = driver.findElement(By.xpath("//span[5]/a[.='IBAN Hesaplama']")).getText(); //iban Hesaplama ekranındaki path'i kaydet
        Assertions.assertEquals("IBAN Hesaplama",ibanHesaplamaPathKontrol);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("\tiban hesaplama sayfasinin acildigi dogrulandi ->> " + ibanHesaplamaPathKontrol);

        driver.findElement(By.xpath("//label[.='Şubeleri İsimlerine Göre Sırala']")).click(); // isme göre sırala seç
        System.out.println("Şubeleri İsimlerine Göre Sırala seçeneği seçildi.");

        WebElement ismeGoreSecimi = driver.findElement(By.xpath("//div[@class='radio-box sort-name']//span[1]"));
        Assertions.assertEquals(driver.findElement(By.xpath("//span[@class='active']")), ismeGoreSecimi);
        System.out.println("\tsube ismine gore sirala seciminin yapildigi goruldu");

        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //şube seçimi için tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='select2-search__field']"))); //şube girişi yapılacak alan açılana kadar bekle
        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys("abana"); //abana için seçim yap
        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='musteriNo']")));
        driver.findElement(By.xpath("//input[@id='musteriNo']")).click(); //müşteri no için tıkla


        //1 ya da 7 haneli random sayi üretme
        int min00 = 1;
        int max00 = 9999999;
        Random rand00 = new Random();
        int intMusteriNo00 = rand00.nextInt(max00-min00) + min00;
        String musteriNo00 = Integer.toString(intMusteriNo00);

        driver.findElement(By.xpath("//input[@id='musteriNo']")).sendKeys(musteriNo00);
        System.out.println("eksik random musteri no girildi: " + musteriNo00);

        driver.findElement(By.xpath("//input[@id='ekNo']")).click(); //ek no alanına tıkla

        //1 ya da 3 haneli random sayı üretme
        int min01 = 1;
        int max01 = 999;
        Random rand01 = new Random();
        int intEkNo01 = rand01.nextInt(max01-min01) + min01;
        String ekNo01 = Integer.toString(intEkNo01);

        driver.findElement(By.xpath("//input[@id='ekNo']")).sendKeys(ekNo01); // ek no gir
        System.out.println("eksik random ek no girildi: " + ekNo01);
        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        String musteriNoHataMesaji = driver.findElement(By.xpath("//label[@id='musteriNo-error']")).getText();
        Assertions.assertEquals("Lütfen en az 8 karakter uzunluğunda bir değer giriniz.",musteriNoHataMesaji);
        System.out.println("\tmusteri no icin hata alindigi goruldu. ->>" + musteriNoHataMesaji);

        String ekNoHataMesaji = driver.findElement(By.xpath("//label[@id='ekNo-error']")).getText();
        Assertions.assertEquals("Lütfen en az 4 karakter uzunluğunda bir değer giriniz.",ekNoHataMesaji);
        System.out.println("\tmusteri no icin hata alindigi goruldu. ->>" + ekNoHataMesaji);

        System.out.println("-------------------------------------------");
    }


        @Test
    void ibanHesaplama_kodaGoreSirala_PassedTest() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("ibanHesaplama_kodaGoreSirala_PassedTest_testi basladi.");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='landingNav']//a[.='IBAN Hesaplama']"))); //iban hesaplama butonunu görene kadar bekle
        driver.findElement(By.xpath("//div[@id='landingNav']//a[.='IBAN Hesaplama']")).click(); //iban hesaplama butonuna tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[5]/a[.='IBAN Hesaplama']"))); //iban hesaplama sayfasındaki path'i görene kadar bekle

        String ibanHesaplamaPathKontrol = driver.findElement(By.xpath("//span[5]/a[.='IBAN Hesaplama']")).getText(); //iban Hesaplama ekranındaki path'i kaydet
        Assertions.assertEquals("IBAN Hesaplama",ibanHesaplamaPathKontrol);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("\tiban hesaplama sayfasinin acildigi dogrulandi ->> " + ibanHesaplamaPathKontrol);

        driver.findElement(By.xpath("//label[.='Şubeleri Kodlarına Göre Sırala']")).click(); //şubeleri kodlarına göre sırala seçeneğini seç
        System.out.println("Şubeleri Kodlarına Göre Sırala seçeneği seçildi.");

        WebElement kodaGoreSecimi = driver.findElement(By.xpath("//div[@class='radio-box sort-code']//span[1]"));
        Assertions.assertEquals(driver.findElement(By.xpath("//span[@class='active']")), kodaGoreSecimi);
        System.out.println("\tsube koduna gore sirala seciminin yapildigi goruldu");
        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //şube seçimi için tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='select2-search__field']"))); //şube girişi yapılacak alan açılana kadar bekle

        //1008 e kadar random sayı üretme
        int min3 = 1;
        int max3 = 1008;
        Random rand3 = new Random();
        int intSubeKodu = rand3.nextInt(max3-min3) + min3;
        String subeKodu = Integer.toString(intSubeKodu);


        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys(subeKodu); //random şube kodu için giriş yap
        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys(Keys.ENTER);
        System.out.println("random sube kodu girildi: " + subeKodu);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='musteriNo']")));
        driver.findElement(By.xpath("//input[@id='musteriNo']")).click(); //müşteri no için tıkla

        //8 ya da 9 haneli random sayi üretme
        int min1 = 10000000;
        int max1 = 999999999;
        Random rand1 = new Random();
        int intMusteriNo = rand1.nextInt(max1-min1) + min1;
        String musteriNo = Integer.toString(intMusteriNo);

        driver.findElement(By.xpath("//input[@id='musteriNo']")).sendKeys(musteriNo);
        System.out.println("random musteri no girildi: " + musteriNo);

        driver.findElement(By.xpath("//input[@id='ekNo']")).click(); //ek no alanına tıkla

        //4 haneli random sayı üretme
        int min2 = 1000;
        int max2 = 9999;
        Random rand2 = new Random();
        int intEkNo = rand2.nextInt(max2-min2) + min2;
        String ekNo = Integer.toString(intEkNo);

        driver.findElement(By.xpath("//input[@id='ekNo']")).sendKeys(ekNo); // ek no gir
        System.out.println("random ek no girildi: " + ekNo);
        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[.='IBAN']"))); // iban yazısını görene kadar bekle

        String ekrandakiIbanYazisi = driver.findElement(By.cssSelector(".result-item > label")).getText();
        Assertions.assertEquals("IBAN", ekrandakiIbanYazisi);
        System.out.println("\tiban olusturuldugu dogrulandi ->> " + ekrandakiIbanYazisi);
        String iban = driver.findElement(By.cssSelector(".result-item > span")).getText();
        System.out.println("iban: " + iban);
        System.out.println("-------------------------------------------");
    }

    @Test
    void ibanHesaplama_kodaGoreSirala_FailedTest() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("ibanHesaplama_kodaGoreSirala_FailedTest_testi basladi.");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='landingNav']//a[.='IBAN Hesaplama']"))); //iban hesaplama butonunu görene kadar bekle
        driver.findElement(By.xpath("//div[@id='landingNav']//a[.='IBAN Hesaplama']")).click(); //iban hesaplama butonuna tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[5]/a[.='IBAN Hesaplama']"))); //iban hesaplama sayfasındaki path'i görene kadar bekle

        String ibanHesaplamaPathKontrol = driver.findElement(By.xpath("//span[5]/a[.='IBAN Hesaplama']")).getText(); //iban Hesaplama ekranındaki path'i kaydet
        Assertions.assertEquals("IBAN Hesaplama",ibanHesaplamaPathKontrol);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("\tiban hesaplama sayfasinin acildigi dogrulandi ->> " + ibanHesaplamaPathKontrol);

        driver.findElement(By.xpath("//label[.='Şubeleri Kodlarına Göre Sırala']")).click(); //şubeleri kodlarına göre sırala seçeneğini seç
        System.out.println("Şubeleri Kodlarına Göre Sırala seçeneği seçildi.");

        WebElement kodaGoreSecimi = driver.findElement(By.xpath("//div[@class='radio-box sort-code']//span[1]"));
        Assertions.assertEquals(driver.findElement(By.xpath("//span[@class='active']")), kodaGoreSecimi);
        System.out.println("\tsube koduna gore sirala seciminin yapildigi goruldu");
        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //şube seçimi için tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='select2-search__field']"))); //şube girişi yapılacak alan açılana kadar bekle

        //1008 e kadar random sayı üretme
        int min3 = 1;
        int max3 = 1008;
        Random rand3 = new Random();
        int intSubeKodu = rand3.nextInt(max3-min3) + min3;
        String subeKodu = Integer.toString(intSubeKodu);


        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys(subeKodu); //random şube kodu için giriş yap
        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys(Keys.ENTER);
        System.out.println("random sube kodu girildi: " + subeKodu);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='musteriNo']")));
        driver.findElement(By.xpath("//input[@id='musteriNo']")).click(); //müşteri no için tıkla


        //1 ya da 7 haneli random sayi üretme
        int min00 = 1;
        int max00 = 9999999;
        Random rand00 = new Random();
        int intMusteriNo00 = rand00.nextInt(max00-min00) + min00;
        String musteriNo00 = Integer.toString(intMusteriNo00);

        driver.findElement(By.xpath("//input[@id='musteriNo']")).sendKeys(musteriNo00);
        System.out.println("eksik random musteri no girildi: " + musteriNo00);

        driver.findElement(By.xpath("//input[@id='ekNo']")).click(); //ek no alanına tıkla

        //1 ya da 3 haneli random sayı üretme
        int min01 = 1;
        int max01 = 999;
        Random rand01 = new Random();
        int intEkNo01 = rand01.nextInt(max01-min01) + min01;
        String ekNo01 = Integer.toString(intEkNo01);

        driver.findElement(By.xpath("//input[@id='ekNo']")).sendKeys(ekNo01); // ek no gir
        System.out.println("eksik random ek no girildi: " + ekNo01);
        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        String musteriNoHataMesaji = driver.findElement(By.xpath("//label[@id='musteriNo-error']")).getText();
        Assertions.assertEquals("Lütfen en az 8 karakter uzunluğunda bir değer giriniz.",musteriNoHataMesaji);
        System.out.println("\tmusteri no icin hata alindigi goruldu. ->>" + musteriNoHataMesaji);

        String ekNoHataMesaji = driver.findElement(By.xpath("//label[@id='ekNo-error']")).getText();
        Assertions.assertEquals("Lütfen en az 4 karakter uzunluğunda bir değer giriniz.",ekNoHataMesaji);
        System.out.println("\tmusteri no icin hata alindigi goruldu. ->>" + ekNoHataMesaji);

        System.out.println("-------------------------------------------");
    }

    @AfterEach
    void teardown () {

        driver.quit();
    }
}
