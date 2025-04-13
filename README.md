# Üretim Firması Sistemi

Bu proje, State Design Pattern, Composite Design Pattern ve GRASP prensiplerine uygun olarak geliştirilmiş bir Üretim Firması Sistemi simülasyonudur.

## Tasarım Kalıpları

### Composite Design Pattern
Sistemdeki bileşenler (hammadde, boya, donanım) ve ürünler (masa, sandalye, vs.) hiyerarşik bir yapıda modellenmiştir. Bu sayede, hem temel bileşenler hem de kompozit ürünler üzerinde tutarlı işlemler yapılabilmektedir.

### State Design Pattern
Üretim süreci farklı durumlar arasında geçiş yaparak yönetilmektedir:
- WaitingForStock: Stok kontrolü
- InManufacturing: Üretim aşaması
- Completed: Tamamlanmış durum
- Failed: Başarısız durum

### GRASP Prensipleri
- Information Expert: Her sınıf kendi verisi üzerinde işlemler yapar
- Creator: Fabrikasyon mantığı ile nesnelerin sorumlu üreticileri tanımlanmıştır
- Controller: Koordinasyon ve yönlendirme sorumluluğu üstlenen sınıflar
- Low Coupling: Sınıflar arası bağımlılık en aza indirilmiştir
- High Cohesion: Her sınıf tek bir sorumluluğa sahiptir

## Proje Yapısı

Proje, geleneksel katmanlı mimariyi uygulamaktadır:

```
src/main/java/com/manufacturing/system/
├── Application.java                  # Ana uygulama sınıfı
├── domain/                           # Domain modelleri ve iş mantığı
│   ├── model/                        # Composite Pattern'ın uygulandığı model sınıfları
│   │   ├── Component.java            # Temel bileşen arayüzü
│   │   ├── BaseComponent.java        # Soyut temel bileşen sınıfı
│   │   ├── RawMaterial.java          # Hammadde sınıfı (Leaf)
│   │   ├── Paint.java                # Boya sınıfı (Leaf)
│   │   ├── Hardware.java             # Donanım sınıfı (Leaf)
│   │   ├── Product.java              # Ürün sınıfı (Composite)
│   │   └── ComponentQuantity.java    # Bileşen-miktar ilişkisi
│   └── state/                        # State Pattern'ın uygulandığı durum sınıfları
│       ├── ManufacturingState.java   # Durum arayüzü
│       ├── ManufacturingProcess.java # Durum bağlamı (Context)
│       ├── WaitingForStockState.java # Stok bekleme durumu
│       ├── InManufacturingState.java # Üretim aşaması durumu
│       ├── CompletedState.java       # Tamamlanmış durum
│       └── FailedState.java          # Başarısız durum
├── application/                      # Uygulama servisleri
│   └── ManufacturingService.java     # Üretim servisi
├── presentation/                     # Sunum katmanı
│   └── ConsoleUI.java                # Konsol kullanıcı arayüzü
├── data/                             # Veri erişim katmanı
│   ├── FileParser.java               # CSV dosya işlemleri
│   ├── ComponentRepository.java      # Bileşen veri erişimi
│   └── ProductRepository.java        # Ürün veri erişimi
└── infrastructure/                   # Altyapı servisleri
    └── RandomGenerator.java          # Rastgele sayı üreteci
```

## Sistem Özellikleri

Sistemde dört ana bileşen türü vardır:
1. Hammadde (Raw Material): Örneğin ahşap levha, metal çubuk
2. Boya (Paint): Örneğin ahşap cilası, mat boya
3. Donanım (Hardware): Örneğin vidalar, menteşeler
4. Ürün (Product): Örneğin masa, sandalye, dolap (diğer bileşenleri içerir)

Üretim süreci şu adımları içerir:
1. Stok kontrolü
2. Üretim aşaması (rastgele sonuç üretilir)
3. Tamamlanma veya başarısızlık durumu (Sistem Hatası, Hasarlı Bileşen, Stok Yetersizliği)

## Gereksinimler

- Java 11 veya üzeri

## Çalıştırma

```bash
mvn clean package
java -jar target/system-1.0-SNAPSHOT.jar
```

## Veri Dosyaları

- `components.csv`: Sistemdeki temel bileşenleri içerir
- `products.csv`: Sistemdeki ürünleri ve bileşen ilişkilerini içerir 