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

public class MevduatGetirisiHesaplama {

    WebDriver driver;

    @BeforeEach
    void setup(){
        driver = new ChromeDriver();


        driver.manage().window().maximize(); //açılan chrome sayfasını tam ekran boyutu yapmak için kullanıldı.
        driver.get("https://www.ziraatbank.com.tr/tr/hesaplama-araclari"); //ziraatbank.com sitesine git

    }

    @Test
    void mevduatGetirisiHesaplamaTesti_TL() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("mevduatGetirisiHesaplamaTesti_TL testi basladi.");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='landingNav']//a[.='Mevduat Getirisi Hesaplama']"))); //mevduat getirisi hesaplama butonunu görene kadar bekle
        driver.findElement(By.xpath("//div[@id='landingNav']//a[.='Mevduat Getirisi Hesaplama']")).click(); //mevduat getirisi hesaplama butonuna tıkla
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[5]/a[.='Mevduat Getirisi Hesaplama']"))); //konut kredisi sayfasındaki yazıyı görene kadar bekle

        String mevduatGetirisiPathKontrol = driver.findElement(By.xpath("//span[5]/a[.='Mevduat Getirisi Hesaplama']")).getText(); //mevduat getirisi Hesaplama ekranındaki path'i kaydet
        Assertions.assertEquals("Mevduat Getirisi Hesaplama",mevduatGetirisiPathKontrol);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("mevduat getirisi hesaplama sayfasinin acildigi dogrulandi ->> " + mevduatGetirisiPathKontrol);

        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //döviz cinsi seçimi için tıkla
        driver.findElement(By.xpath("//li[@class='select2-results__option select2-results__option--highlighted']")).click(); //TL seçimi yap
        System.out.println("TL secimi yapildi");

        String tlSecimiDogrulama = driver.findElement(By.cssSelector(".form-box .form-item > .input > i")).getText();
        Assertions.assertEquals("TL",tlSecimiDogrulama);
        System.out.println("TL secimi yapildigi dogrulandi ->> " + tlSecimiDogrulama);

        WebElement vadeSuresiSlider = driver.findElement(By.xpath("//div[@class='slider-calculate slider-vade ui-slider ui-corner-all ui-slider-horizontal ui-widget ui-widget-content']")); //vade süresi slider için tanım yapıldı
        Actions action = new Actions(driver); // action tanımlandı
        action.dragAndDropBy(vadeSuresiSlider,50,0).perform(); //slider x ekseninde 50, y eksinin 0 olacak şekilde hareket ettirildi.

        System.out.println("slider ile secim yapildi");

        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).click();
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys("750");
        System.out.println("vade suresi olarak sinirlarin disinda bir deger(750) girildigi zaman 730 olarak guncellendigi goruldu.");
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).click();
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys("365");
        System.out.println("vade suresi olarak sinirlar dahilinde bir deger(365) girilebildigi goruldu.");

        driver.findElement(By.xpath("//input[@id='tutar']")).click(); //tutar alanına tıkla
        driver.findElement(By.xpath("//input[@id='tutar']")).sendKeys("1000000"); // tutar gir
        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[.='Vade Sonu Tutarı']"))); // vade sonu tutarı yazısını görene kadar bekle
        String hesaplaSonrasiCikanYazi = driver.findElement(By.xpath("//label[.='Vade Sonu Tutarı']")).getText();
        Assertions.assertEquals("Vade Sonu Tutarı",hesaplaSonrasiCikanYazi);
        System.out.println("mevduat getirisinin hesaplandigi goruldu.");

        String vadeSonuTutari = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(1) > span")).getText();
        System.out.println("vade sonu tutari: " + vadeSonuTutari);
        String faizOrani = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(2) > span")).getText();
        System.out.println("faiz orani: " + faizOrani);
        System.out.println("-------------------------------------------");
    }

    @Test
    void mevduatGetirisiHesaplamaTesti_USD() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("mevduatGetirisiHesaplamaTesti_USD testi basladi.");
        //driver.findElement(By.xpath("//div[@id='landingNav']//a[.='IBAN Hesaplama']")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='landingNav']//a[.='Mevduat Getirisi Hesaplama']"))); //mevduat getirisi hesaplama butonunu görene kadar bekle
        driver.findElement(By.xpath("//div[@id='landingNav']//a[.='Mevduat Getirisi Hesaplama']")).click(); //mevduat getirisi hesaplama butonuna tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[5]/a[.='Mevduat Getirisi Hesaplama']"))); //konut kredisi sayfasındaki yazıyı görene kadar bekle

        String mevduatGetirisiPathKontrol = driver.findElement(By.xpath("//span[5]/a[.='Mevduat Getirisi Hesaplama']")).getText(); //mevduat getirisi Hesaplama ekranındaki path'i kaydet
        Assertions.assertEquals("Mevduat Getirisi Hesaplama",mevduatGetirisiPathKontrol);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("mevduat getirisi hesaplama sayfasinin acildigi dogrulandi ->> " + mevduatGetirisiPathKontrol);

        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //döviz cinsi seçimi için tıkla
        driver.findElement(By.xpath("//li[.='USD']")).click(); //USD seçimi yap
        System.out.println("USD seicim yapildi");

        String usdSecimiDogrulama = driver.findElement(By.cssSelector(".form-box .form-item > .input > i")).getText();
        Assertions.assertEquals("USD",usdSecimiDogrulama);
        System.out.println("USD secimi yapildigi dogrulandi ->> " + usdSecimiDogrulama);

        WebElement vadeSuresiSlider = driver.findElement(By.xpath("//div[@class='slider-calculate slider-vade ui-slider ui-corner-all ui-slider-horizontal ui-widget ui-widget-content']")); //vade süresi slider için tanım yapıldı
        Actions action = new Actions(driver); // action tanımlandı
        action.dragAndDropBy(vadeSuresiSlider,50,0).perform(); //slider x ekseninde 50, y eksinin 0 olacak şekilde hareket ettirildi.

        System.out.println("slider ile secim yapildi");

        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).click();
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys("750");
        System.out.println("vade suresi olarak sinirlarin disinda bir deger(750) girildigi zaman 730 olarak guncellendigi goruldu.");
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).click();
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys("365");
        System.out.println("vade suresi olarak sinirlar dahilinde bir deger(365) girilebildigi goruldu.");

        driver.findElement(By.xpath("//input[@id='tutar']")).click(); //tutar alanına tıkla
        driver.findElement(By.xpath("//input[@id='tutar']")).sendKeys("1000000"); // tutar gir
        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[.='Vade Sonu Tutarı']"))); // vade sonu tutarı yazısını görene kadar bekle
        String hesaplaSonrasiCikanYazi = driver.findElement(By.xpath("//label[.='Vade Sonu Tutarı']")).getText();
        Assertions.assertEquals("Vade Sonu Tutarı",hesaplaSonrasiCikanYazi);
        System.out.println("mevduat getirisinin hesaplandigi goruldu.");

        String vadeSonuTutari = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(1) > span")).getText();
        System.out.println("vade sonu tutari: " + vadeSonuTutari);
        String faizOrani = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(2) > span")).getText();
        System.out.println("faiz orani: " + faizOrani);
        System.out.println("-------------------------------------------");
    }

    @Test
    void mevduatGetirisiHesaplamaTesti_EURO() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20L)); //bu method sayesinde static wait olan thread yerine dinamic wait kullanıyoruz.

        System.out.println("mevduatGetirisiHesaplamaTesti_EURO testi basladi.");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='landingNav']//a[.='Mevduat Getirisi Hesaplama']"))); //mevduat getirisi hesaplama butonunu görene kadar bekle
        driver.findElement(By.xpath("//div[@id='landingNav']//a[.='Mevduat Getirisi Hesaplama']")).click(); //mevduat getirisi hesaplama butonuna tıkla
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[5]/a[.='Mevduat Getirisi Hesaplama']"))); //konut kredisi sayfasındaki yazıyı görene kadar bekle

        String mevduatGetirisiPathKontrol = driver.findElement(By.xpath("//span[5]/a[.='Mevduat Getirisi Hesaplama']")).getText(); //mevduat getirisi Hesaplama ekranındaki path'i kaydet
        Assertions.assertEquals("Mevduat Getirisi Hesaplama",mevduatGetirisiPathKontrol);//butona tıkladıktan sonra, sayfanın açıldığını anlayabilmek için beklenen yazı ile çıkan sonuçu kıyasla
        System.out.println("mevduat getirisi hesaplama sayfasinin acildigi dogrulandi ->> " + mevduatGetirisiPathKontrol);

        driver.findElement(By.xpath("//span[@class='select2-selection__arrow']")).click(); //döviz cinsi seçimi için tıkla
        driver.findElement(By.xpath("//li[.='EURO']")).click(); //EURO seçimi yap
        System.out.println("EURO seicim yapildi");

        String usdSecimiDogrulama = driver.findElement(By.cssSelector(".form-box .form-item > .input > i")).getText();
        Assertions.assertEquals("EURO",usdSecimiDogrulama);
        System.out.println("EURO secimi yapildigi dogrulandi ->> " + usdSecimiDogrulama);

        WebElement vadeSuresiSlider = driver.findElement(By.xpath("//div[@class='slider-calculate slider-vade ui-slider ui-corner-all ui-slider-horizontal ui-widget ui-widget-content']")); //vade süresi slider için tanım yapıldı
        Actions action = new Actions(driver); // action tanımlandı
        action.dragAndDropBy(vadeSuresiSlider,50,0).perform(); //slider x ekseninde 50, y eksinin 0 olacak şekilde hareket ettirildi.

        System.out.println("slider ile secim yapildi");

        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).click();
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys("750");
        System.out.println("vade suresi olarak sinirlarin disinda bir deger(750) girildigi zaman 730 olarak guncellendigi goruldu.");
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).click();
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys(Keys.DELETE);
        driver.findElement(By.xpath("//input[@id='deposit-calc-vade']")).sendKeys("365");
        System.out.println("vade suresi olarak sinirlar dahilinde bir deger(365) girilebildigi goruldu.");

        driver.findElement(By.xpath("//input[@id='tutar']")).click(); //tutar alanına tıkla
        driver.findElement(By.xpath("//input[@id='tutar']")).sendKeys("1000000"); // tutar gir
        driver.findElement(By.xpath("//a[.='HESAPLA']")).click(); //hesapla butonuna tıkla

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[.='Vade Sonu Tutarı']"))); // vade sonu tutarı yazısını görene kadar bekle
        String hesaplaSonrasiCikanYazi = driver.findElement(By.xpath("//label[.='Vade Sonu Tutarı']")).getText();
        Assertions.assertEquals("Vade Sonu Tutarı",hesaplaSonrasiCikanYazi);
        System.out.println("mevduat getirisinin hesaplandigi goruldu.");

        String vadeSonuTutari = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(1) > span")).getText();
        System.out.println("vade sonu tutari: " + vadeSonuTutari);
        String faizOrani = driver.findElement(By.cssSelector(".result-right > div:nth-of-type(2) > span")).getText();
        System.out.println("faiz orani: " + faizOrani);
        System.out.println("-------------------------------------------");
    }

    @AfterEach
    void teardown () {

        driver.quit();
    }
}
