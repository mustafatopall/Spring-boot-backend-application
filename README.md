# Spring Boot Backend API

Modern ve profesyonel bir Spring Boot backend uygulaması. RESTful API, JPA, H2 veritabanı, Swagger dokümantasyonu, pagination, logging ve unit testler içeren tam özellikli bir backend projesi.

##  Özellikler

-  **RESTful API** - Tam CRUD operasyonları
-  **JPA & Hibernate** - Veritabanı ORM
-  **H2 Database** - In-memory veritabanı (geliştirme için)
-  **DTO Pattern** - Data Transfer Objects
-  **Validation** - Bean Validation ile request doğrulama
-  **Exception Handling** - Global exception handler
-  **Pagination** - Sayfalama desteği
-  **Search/Filtering** - Arama ve filtreleme
-  **Swagger/OpenAPI** - API dokümantasyonu
-  **Logging** - Logback ile detaylı loglama
-  **Unit Tests** - Mockito ile testler
-  **CORS** - Cross-Origin Resource Sharing desteği

##  Teknolojiler

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **SpringDoc OpenAPI (Swagger)**
- **Logback**
- **Mockito** (Test)
- **Maven**

##  Gereksinimler

- Java 17 veya üzeri
- Maven 3.6+

##  Kurulum

### 1. Projeyi Klonlayın

```bash
git clone https://github.com/mustafatopall/spring-boot-backend-application.git
cd spring-boot-backend
```

### 2. Bağımlılıkları Yükleyin

```bash
./mvnw clean install
```

### 3. Uygulamayı Çalıştırın

```bash
./mvnw spring-boot:run
```

Uygulama `http://localhost:8080` adresinde başlayacaktır.

##  Kullanım

### API Base URL
```
http://localhost:8080/api
```

### Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### H2 Console
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (boş)

##  API Endpoints

### Users

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/api/users` | Tüm kullanıcıları listele |
| GET | `/api/users/page` | Sayfalanmış kullanıcı listesi |
| GET | `/api/users/{id}` | ID'ye göre kullanıcı getir |
| POST | `/api/users` | Yeni kullanıcı oluştur |
| PUT | `/api/users/{id}` | Kullanıcı güncelle |
| DELETE | `/api/users/{id}` | Kullanıcı sil |

### Posts

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/api/posts` | Tüm postları listele |
| GET | `/api/posts/page` | Sayfalanmış post listesi |
| GET | `/api/posts/{id}` | ID'ye göre post getir |
| GET | `/api/posts/user/{userId}` | Kullanıcının postlarını listele |
| GET | `/api/posts/user/{userId}/page` | Sayfalanmış kullanıcı postları |
| GET | `/api/posts/search?keyword=...` | Post ara |
| GET | `/api/posts/search/page?keyword=...` | Sayfalanmış arama |
| POST | `/api/posts` | Yeni post oluştur |
| PUT | `/api/posts/{id}` | Post güncelle |
| DELETE | `/api/posts/{id}` | Post sil |

##  Kullanım Örnekleri

### Kullanıcı Oluşturma

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "mustafa@example.com",
    "name": "Mustafa",
    "surname": "Topal"
  }'
```

### Post Oluşturma

```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "İlk Post",
    "content": "Bu benim ilk postum.",
    "userId": 1
  }'
```

### Pagination ile Listeleme

```bash
curl "http://localhost:8080/api/users/page?page=0&size=10&sortBy=id&sortDir=asc"
```

### Post Arama

```bash
curl "http://localhost:8080/api/posts/search?keyword=Spring"
```

##  Proje Yapısı

```
src/
├── main/
│   ├── java/com/mustafatopalearning/spring/
│   │   ├── config/          # Konfigürasyon sınıfları
│   │   ├── controller/      # REST Controller'lar
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA Entity'ler
│   │   ├── exception/       # Exception handler'lar
│   │   ├── repository/      # JPA Repository'ler
│   │   └── service/         # Service katmanı
│   └── resources/
│       ├── application.properties
│       └── logback-spring.xml
└── test/
    └── java/com/mustafatopalearning/spring/
        ├── controller/      # Controller testleri
        └── service/         # Service testleri
```

##  Test

### Tüm Testleri Çalıştırma

```bash
./mvnw test
```

### Test Coverage

Proje Mockito ile unit testler içermektedir. Testler şunları kapsar:
- UserService testleri
- PostService testleri
- UserController testleri
- PostController testleri

##  Validation

API endpoint'leri Bean Validation kullanarak request doğrulaması yapar:

- **Email**: Geçerli email formatı kontrolü
- **Name/Surname**: Minimum 2, maksimum 50 karakter
- **Title**: Minimum 3, maksimum 200 karakter
- **Content**: Minimum 10 karakter

##  Response Format

Tüm API yanıtları standart bir format kullanır:

```json
{
  "success": true,
  "message": "İşlem başarılı",
  "data": {
    // Response data
  }
}
```

### Hata Yanıtı

```json
{
  "success": false,
  "message": "Hata mesajı",
  "data": null
}
```

##  Pagination

Pagination endpoint'leri aşağıdaki parametreleri kabul eder:

- `page`: Sayfa numarası (varsayılan: 0)
- `size`: Sayfa başına kayıt sayısı (varsayılan: 10)
- `sortBy`: Sıralama alanı (varsayılan: id veya createdAt)
- `sortDir`: Sıralama yönü (asc/desc, varsayılan: asc)

### Pagination Response

```json
{
  "success": true,
  "message": "İşlem başarılı",
  "data": {
    "content": [...],
    "page": 0,
    "size": 10,
    "totalElements": 100,
    "totalPages": 10,
    "first": true,
    "last": false
  }
}
```

##  Veritabanı

Proje H2 in-memory veritabanı kullanmaktadır. Veritabanı şeması JPA tarafından otomatik olarak oluşturulur.

### Entity İlişkileri

- **User** (1) ──< (**Many**) Post
- Bir kullanıcının birden fazla postu olabilir
- Kullanıcı silindiğinde postları da silinir (Cascade DELETE)

##  Exception Handling

Proje global exception handler içerir:

- `ResourceNotFoundException`: Kaynak bulunamadığında (404)
- `BadRequestException`: Geçersiz isteklerde (400)
- `MethodArgumentNotValidException`: Validation hatalarında (400)

##  Logging

Logback konfigürasyonu ile detaylı loglama yapılır:

- Console logging
- File logging (log dosyası: `spring.log`)
- Log seviyeleri: DEBUG, INFO, WARN, ERROR
- SQL sorguları loglanır (DEBUG seviyesinde)

##  Konfigürasyon

### application.properties

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## 📄 Lisans

Bu proje açık kaynaklıdır ve serbestçe kullanılabilir.

##  Geliştirici

**Mustafa Topal**

- GitHub: [@mustafatopall](https://github.com/mustafatopall)

##  Teşekkürler

- Spring Boot ekibine

