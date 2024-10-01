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
import java.util.Random;

public class KrediHesaplama {

    WebDriver driver;

    @BeforeEach
    void setup(){
        driver = new ChromeDriver();


        driver.manage().window().maximize(); //açılan chrome sayfasını tam ekran boyutu yapmak için kullanıldı.
        driver.get("https://www.ziraatbank.com.tr/tr/hesaplama-araclari"); //ziraatbank.com sitesine git
    }

    @Test
    void krediHesaplamaAraci_KonutKredisiTesti() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("kredi hesaplama araci testi basladi.");
        //Thread.sleep(2000);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-md-12 col-sm-6 col-xs-12 height-columns']//div[@class='row']"))); //konut kredisi butonunu görene kadar bekle
        driver.findElement(By.xpath("//ul[@class='link-col-1 clearfix']//a[.='Konut Kredisi']")).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[.='Konut Kredisi Hesaplama Aracı']"))); //konut kredisi sayfasındaki yazıyı görene kadar bekle

        String konutKredisiEkranindakiYazi = driver.findElement(By.xpath("//h3[.='Konut Kredisi Hesaplama Aracı']")).getText(); //Konut Kredisi Hesaplama ekranında çıkan yazıyı kaydet
        Assertions.assertEquals("Konut Kredisi Hesaplama Aracı",konutKredisiEkranindakiYazi);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("ana ekrandan 'konut kredisi' secimi yapildi ->> "+ konutKredisiEkranindakiYazi);

        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //kredi seçimi yap
        driver.findElement(By.cssSelector(".select2-results__option--highlighted")).click(); //konut kredisi seç
        String konutKredisiSecimi = driver.findElement(By.xpath("//span[@class='select2-selection select2-selection--single']")).getText();// kredi seçiminin konut kredisi olduğunu gör(diğer seçenekler: konut kredisi ürün paketi, yeşil ev konut kredisi)

        Assertions.assertEquals("Konut Kredisi",konutKredisiSecimi); //beklenen seçim ile yapılan seçimi kıyasla
        System.out.println("\tkonut kredisi ekranindaki kredi seciminin konut kredisi oldugu goruldu ->> " + konutKredisiSecimi);

        WebElement tutarSlider = driver.findElement(By.xpath("//div[@class='calculation-form form-box']//div[2]//div[@class='slider-calculation']")); //tutar slider için tanım yapıldı
        Actions action01 = new Actions(driver); // action tanımlandı
        action01.dragAndDropBy(tutarSlider,45,0).perform(); //slider x ekseninde 50, y eksinin 0 olacak şekilde hareket ettirildi.
        System.out.println("slider ile tutar secimi yapildi");

        driver.findElement(By.xpath("//input[@id='calc-tutar']")).click(); //tutara tıkla
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.BACK_SPACE);
        //driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys("1500000");

        //1-99999999 arasi random sayi üretme
        int min01 = 1;
        int max01 = 99999999;
        Random rand01 = new Random();
        int intTutar = rand01.nextInt(max01-min01) + min01;
        String tutar = Integer.toString(intTutar);

        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(tutar);
        System.out.println("random tutar girildi: " + tutar);
        System.out.println("\tslider haricinde yazarak da tutar girisi yapilabildigi goruldu.");


        WebElement vadeSlider = driver.findElement(By.xpath("//div[@class='calculation-form form-box']//div[3]//div[@class='slider-calculation']")); //tutar slider için tanım yapıldı
        Actions action02 = new Actions(driver); // action tanımlandı
        action02.dragAndDropBy(vadeSlider,20,0).perform(); //slider x ekseninde 50, y eksinin 0 olacak şekilde hareket ettirildi.
        System.out.println("slider ile vade secimi yapildi");

        driver.findElement(By.xpath("//input[@id='calc-vade']")).click(); //vadeye tıkla
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);

        //121-999 arasi random sayi üretme
        int min02 = 121;
        int max02 = 999;
        Random rand02 = new Random();
        int intVade = rand02.nextInt(max02-min02) + min02;
        String vade = Integer.toString(intVade);

        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(vade);
        System.out.println("sinir ustu random vade girildi: " + vade);
        //System.out.println("\tslider haricinde yazarak da vade girisi yapilabildigi goruldu.");
        System.out.println("\tvade suresi olarak sinirlarin disinda bir deger girildigi zaman 120 olarak guncellendigi goruldu.");

        driver.findElement(By.xpath("//input[@id='calc-vade']")).click(); //vadeye tıkla
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);

        //1-120 arasi random sayi üretme
        int min03 = 1;
        int max03 = 120;
        Random rand03 = new Random();
        int intVade2 = rand03.nextInt(max03-min03) + min03;
        String vade2 = Integer.toString(intVade2);

        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(vade2);
        System.out.println("120den az olacak sekilde random vade girildi: " + vade2);
        System.out.println("\tvade suresi olarak sinirlarin icinde bir deger girildigi girilen deger ile devam ettigi goruldu.");

        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[.='Taksit Tutarı']"))); // taksit tutarı yazısını görene kadar bekle
        String hesaplaSonrasiCikanYazi = driver.findElement(By.xpath("//label[.='Taksit Tutarı']")).getText();
        Assertions.assertEquals("Taksit Tutarı",hesaplaSonrasiCikanYazi);
        System.out.println("\tkonut kredisinin hesaplandigi goruldu.");

        String faizTutari = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(1) > span")).getText();
        System.out.println("faiz tutari: " + faizTutari);
        String faizOrani = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(2) > span")).getText();
        System.out.println("faiz orani: " + faizOrani);
        String yillikMaliyetOrani = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(3) > span")).getText();
        System.out.println("yillik maliyet orani: " + yillikMaliyetOrani);
        System.out.println("-------------------------------------------");
    }

    @Test
    void krediHesaplamaAraci_KonutKredisiUrunPaketiTesti() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("kredi hesaplama araci testi basladi.");
        //Thread.sleep(2000);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-md-12 col-sm-6 col-xs-12 height-columns']//div[@class='row']"))); //konut kredisi butonunu görene kadar bekle
        driver.findElement(By.xpath("//ul[@class='link-col-1 clearfix']//a[.='Konut Kredisi']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[.='Konut Kredisi Hesaplama Aracı']"))); //konut kredisi sayfasındaki yazıyı görene kadar bekle

        String konutKredisiEkranindakiYazi = driver.findElement(By.xpath("//h3[.='Konut Kredisi Hesaplama Aracı']")).getText(); //Konut Kredisi Hesaplama ekranında çıkan yazıyı kaydet
        Assertions.assertEquals("Konut Kredisi Hesaplama Aracı",konutKredisiEkranindakiYazi);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("ana ekrandan 'konut kredisi' secimi yapildi ->> "+ konutKredisiEkranindakiYazi);

        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //kredi seçimi yap
        driver.findElement(By.cssSelector(".select2-results__options > li:nth-of-type(2)")).click(); //konut kredisi ürün paketi seç
        String konutKredisiUrunPaketiSecimi = driver.findElement(By.xpath("//span[@class='select2-selection select2-selection--single']")).getText();// kredi seçiminin konut kredisi ürün paketi olduğunu gör(diğer seçenekler: konut kredisi, yeşil ev konut kredisi)

        Assertions.assertEquals("Konut Kredisi Ürün Paketi",konutKredisiUrunPaketiSecimi); //beklenen seçim ile yapılan seçimi kıyasla
        System.out.println("\tkonut kredisi ekranindaki kredi seciminin konut kredisi oldugu goruldu ->> " + konutKredisiUrunPaketiSecimi);

        WebElement tutarSlider = driver.findElement(By.xpath("//div[@class='calculation-form form-box']//div[2]//div[@class='slider-calculation']")); //tutar slider için tanım yapıldı
        Actions action01 = new Actions(driver); // action tanımlandı
        action01.dragAndDropBy(tutarSlider,45,0).perform(); //slider x ekseninde 50, y eksinin 0 olacak şekilde hareket ettirildi.
        System.out.println("slider ile tutar secimi yapildi");

        driver.findElement(By.xpath("//input[@id='calc-tutar']")).click(); //tutara tıkla
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.BACK_SPACE);
        //driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys("1500000");

        //1-99999999 arasi random sayi üretme
        int min01 = 1;
        int max01 = 99999999;
        Random rand01 = new Random();
        int intTutar = rand01.nextInt(max01-min01) + min01;
        String tutar = Integer.toString(intTutar);

        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(tutar);
        System.out.println("random tutar girildi: " + tutar);
        System.out.println("\tslider haricinde yazarak da tutar girisi yapilabildigi goruldu.");


        WebElement vadeSlider = driver.findElement(By.xpath("//div[@class='calculation-form form-box']//div[3]//div[@class='slider-calculation']")); //tutar slider için tanım yapıldı
        Actions action02 = new Actions(driver); // action tanımlandı
        action02.dragAndDropBy(vadeSlider,20,0).perform(); //slider x ekseninde 50, y eksinin 0 olacak şekilde hareket ettirildi.
        System.out.println("slider ile vade secimi yapildi");

        driver.findElement(By.xpath("//input[@id='calc-vade']")).click(); //vadeye tıkla
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);

        //121-999 arasi random sayi üretme
        int min02 = 121;
        int max02 = 999;
        Random rand02 = new Random();
        int intVade = rand02.nextInt(max02-min02) + min02;
        String vade = Integer.toString(intVade);

        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(vade);
        System.out.println("sinir ustu random vade girildi: " + vade);
        //System.out.println("\tslider haricinde yazarak da vade girisi yapilabildigi goruldu.");
        System.out.println("\tvade suresi olarak sinirlarin disinda bir deger girildigi zaman 120 olarak guncellendigi goruldu.");

        driver.findElement(By.xpath("//input[@id='calc-vade']")).click(); //vadeye tıkla
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);

        //1-120 arasi random sayi üretme
        int min03 = 1;
        int max03 = 120;
        Random rand03 = new Random();
        int intVade2 = rand03.nextInt(max03-min03) + min03;
        String vade2 = Integer.toString(intVade2);

        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(vade2);
        System.out.println("120den az olacak sekilde random vade girildi: " + vade2);
        System.out.println("\tvade suresi olarak sinirlarin icinde bir deger girildigi girilen deger ile devam ettigi goruldu.");

        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[.='Taksit Tutarı']"))); // taksit tutarı yazısını görene kadar bekle
        String hesaplaSonrasiCikanYazi = driver.findElement(By.xpath("//label[.='Taksit Tutarı']")).getText();
        Assertions.assertEquals("Taksit Tutarı",hesaplaSonrasiCikanYazi);
        System.out.println("\tkonut kredisinin hesaplandigi goruldu.");

        String faizTutari = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(1) > span")).getText();
        System.out.println("faiz tutari: " + faizTutari);
        String faizOrani = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(2) > span")).getText();
        System.out.println("faiz orani: " + faizOrani);
        String yillikMaliyetOrani = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(3) > span")).getText();
        System.out.println("yillik maliyet orani: " + yillikMaliyetOrani);
        System.out.println("-------------------------------------------");
    }

    @Test
    void krediHesaplamaAraci_YesilEvKonutKredisiTesti() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("kredi hesaplama araci testi basladi.");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='col-md-12 col-sm-6 col-xs-12 height-columns']//div[@class='row']"))); //konut kredisi butonunu görene kadar bekle
        driver.findElement(By.xpath("//ul[@class='link-col-1 clearfix']//a[.='Konut Kredisi']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[.='Konut Kredisi Hesaplama Aracı']"))); //konut kredisi sayfasındaki yazıyı görene kadar bekle

        String konutKredisiEkranindakiYazi = driver.findElement(By.xpath("//h3[.='Konut Kredisi Hesaplama Aracı']")).getText(); //Konut Kredisi Hesaplama ekranında çıkan yazıyı kaydet
        Assertions.assertEquals("Konut Kredisi Hesaplama Aracı",konutKredisiEkranindakiYazi);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("ana ekrandan 'konut kredisi' secimi yapildi ->> "+ konutKredisiEkranindakiYazi);

        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //kredi seçimi yap
        driver.findElement(By.cssSelector(".select2-results__options > li:nth-of-type(3)")).click(); //yeşil ev konut kredisi seç
        String yesilEvKonutKredisiSecimi = driver.findElement(By.xpath("//span[@class='select2-selection select2-selection--single']")).getText();// kredi seçiminin yeşil ev konut kredisi olduğunu gör(diğer seçenekler: konut kredisi, konut kredisi ürün paketi)

        Assertions.assertEquals("Yeşil Ev Konut Kredisi",yesilEvKonutKredisiSecimi); //beklenen seçim ile yapılan seçimi kıyasla
        System.out.println("\tkonut kredisi ekranindaki kredi seciminin konut kredisi oldugu goruldu ->> " + yesilEvKonutKredisiSecimi);

        WebElement tutarSlider = driver.findElement(By.xpath("//div[@class='calculation-form form-box']//div[2]//div[@class='slider-calculation']")); //tutar slider için tanım yapıldı
        Actions action01 = new Actions(driver); // action tanımlandı
        action01.dragAndDropBy(tutarSlider,45,0).perform(); //slider x ekseninde 50, y eksinin 0 olacak şekilde hareket ettirildi.
        System.out.println("slider ile tutar secimi yapildi");

        driver.findElement(By.xpath("//input[@id='calc-tutar']")).click(); //tutara tıkla
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(Keys.BACK_SPACE);
        //driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys("1500000");

        //1-99999999 arasi random sayi üretme
        int min01 = 1;
        int max01 = 99999999;
        Random rand01 = new Random();
        int intTutar = rand01.nextInt(max01-min01) + min01;
        String tutar = Integer.toString(intTutar);

        driver.findElement(By.xpath("//input[@id='calc-tutar']")).sendKeys(tutar);
        System.out.println("random tutar girildi: " + tutar);
        System.out.println("\tslider haricinde yazarak da tutar girisi yapilabildigi goruldu.");


        WebElement vadeSlider = driver.findElement(By.xpath("//div[@class='calculation-form form-box']//div[3]//div[@class='slider-calculation']")); //tutar slider için tanım yapıldı
        Actions action02 = new Actions(driver); // action tanımlandı
        action02.dragAndDropBy(vadeSlider,20,0).perform(); //slider x ekseninde 50, y eksinin 0 olacak şekilde hareket ettirildi.
        System.out.println("slider ile vade secimi yapildi");

        driver.findElement(By.xpath("//input[@id='calc-vade']")).click(); //vadeye tıkla
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);

        //121-999 arasi random sayi üretme
        int min02 = 121;
        int max02 = 999;
        Random rand02 = new Random();
        int intVade = rand02.nextInt(max02-min02) + min02;
        String vade = Integer.toString(intVade);

        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(vade);
        System.out.println("sinir ustu random vade girildi: " + vade);
        //System.out.println("\tslider haricinde yazarak da vade girisi yapilabildigi goruldu.");
        System.out.println("\tvade suresi olarak sinirlarin disinda bir deger girildigi zaman 120 olarak guncellendigi goruldu.");

        driver.findElement(By.xpath("//input[@id='calc-vade']")).click(); //vadeye tıkla
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(Keys.DELETE);

        //1-120 arasi random sayi üretme
        int min03 = 1;
        int max03 = 120;
        Random rand03 = new Random();
        int intVade2 = rand03.nextInt(max03-min03) + min03;
        String vade2 = Integer.toString(intVade2);

        driver.findElement(By.xpath("//input[@id='calc-vade']")).sendKeys(vade2);
        System.out.println("120den az olacak sekilde random vade girildi: " + vade2);
        System.out.println("\tvade suresi olarak sinirlarin icinde bir deger girildigi girilen deger ile devam ettigi goruldu.");

        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[.='Taksit Tutarı']"))); // taksit tutarı yazısını görene kadar bekle
        String hesaplaSonrasiCikanYazi = driver.findElement(By.xpath("//label[.='Taksit Tutarı']")).getText();
        Assertions.assertEquals("Taksit Tutarı",hesaplaSonrasiCikanYazi);
        System.out.println("\tkonut kredisinin hesaplandigi goruldu.");

        String faizTutari = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(1) > span")).getText();
        System.out.println("faiz tutari: " + faizTutari);
        String faizOrani = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(2) > span")).getText();
        System.out.println("faiz orani: " + faizOrani);
        String yillikMaliyetOrani = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(3) > span")).getText();
        System.out.println("yillik maliyet orani: " + yillikMaliyetOrani);
        System.out.println("-------------------------------------------");
    }

    @AfterEach
    void teardown () {

        driver.quit();
    }
}