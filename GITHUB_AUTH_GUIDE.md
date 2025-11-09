# GitHub Authentication Rehberi

## Problem
GitHub artık password authentication'ı desteklemiyor. Personal Access Token (PAT) veya SSH kullanmanız gerekiyor.

## Çözüm 1: Personal Access Token (PAT) - Önerilen ✅

### Adım 1: Personal Access Token Oluştur

1. GitHub'a gidin: https://github.com
2. Sağ üst köşeden **Settings** (Ayarlar) tıklayın
3. Sol menüden **Developer settings** seçin
4. **Personal access tokens** > **Tokens (classic)** seçin
5. **Generate new token** > **Generate new token (classic)** tıklayın
6. Token adı verin: `spring-boot-project` (veya istediğiniz bir isim)
7. Expiration (Süre): 90 days (veya istediğiniz süre)
8. Scopes (İzinler) - Şunları seçin:
   - ✅ **repo** (Tüm repo işlemleri için)
     - repo:status
     - repo_deployment
     - public_repo
     - repo:invite
     - security_events
9. **Generate token** butonuna tıklayın
10. **ÖNEMLİ:** Token'ı kopyalayın (bir daha gösterilmeyecek!)
    - Token şöyle görünür: `ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx`

### Adım 2: Token'ı Kullanarak Push Yap

```bash
cd /Users/mustafatopal/Downloads/spring

# Remote URL'i kontrol et
git remote -v

# Eğer HTTPS kullanıyorsanız, token ile push yapın
git push -u origin main
```

Push yaparken:
- **Username**: GitHub kullanıcı adınız (`mustafatopall`)
- **Password**: Token'ı yapıştırın (`ghp_xxxxxxxx...`)

### Adım 3: Token'ı Git Credential Helper'da Sakla (Opsiyonel)

Token'ı her seferinde girmemek için:

```bash
# macOS için
git config --global credential.helper osxkeychain

# Linux için
git config --global credential.helper store

# Windows için
git config --global credential.helper wincred
```

Sonra bir kez push yaptığınızda token saklanır.

## Çözüm 2: SSH Kullan (Alternatif) 🔑

### Adım 1: SSH Key Oluştur

```bash
# SSH key oluştur (eğer yoksa)
ssh-keygen -t ed25519 -C "your_email@example.com"

# Enter'a basın (default location)
# Passphrase girebilirsiniz (opsiyonel, boş bırakabilirsiniz)

# SSH key'i görüntüle
cat ~/.ssh/id_ed25519.pub
```

### Adım 2: SSH Key'i GitHub'a Ekle

1. GitHub'a gidin: https://github.com/settings/keys
2. **New SSH key** tıklayın
3. **Title**: `MacBook` (veya bilgisayarınızın adı)
4. **Key**: `cat ~/.ssh/id_ed25519.pub` komutunun çıktısını yapıştırın
5. **Add SSH key** tıklayın

### Adım 3: Remote URL'i SSH'ye Çevir

```bash
# Mevcut remote'u kontrol et
git remote -v

# Remote URL'i SSH'ye çevir
git remote set-url origin git@github.com:mustafatopall/spring-boot-backend-application.git

# Kontrol et
git remote -v
```

### Adım 4: SSH Bağlantısını Test Et

```bash
ssh -T git@github.com
```

Şöyle bir mesaj görmelisiniz:
```
Hi mustafatopall! You've successfully authenticated, but GitHub does not provide shell access.
```

### Adım 5: Push Yap

```bash
git push -u origin main
```

Artık password veya token girmeden push yapabilirsiniz!

## Hızlı Çözüm (PAT ile)

Eğer hızlıca push yapmak istiyorsanız:

```bash
cd /Users/mustafatopal/Downloads/spring

# Remote URL'i kontrol et
git remote -v

# Eğer HTTPS ise, token ile push yap
git push -u origin main
```

Push istenirken:
- Username: `mustafatopall`
- Password: `ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx` (token'ınız)

## Troubleshooting

### Token çalışmıyor
- Token'ın `repo` scope'una sahip olduğundan emin olun
- Token'ın süresi dolmamış olmalı
- Token'ı doğru kopyaladığınızdan emin olun

### SSH çalışmıyor
```bash
# SSH agent'ı başlat
eval "$(ssh-agent -s)"

# SSH key'i ekle
ssh-add ~/.ssh/id_ed25519

# Test et
ssh -T git@github.com
```

### Remote URL yanlış
```bash
# Remote URL'i görüntüle
git remote -v

# HTTPS'e çevir
git remote set-url origin https://github.com/mustafatopall/spring-boot-backend-application.git

# SSH'ye çevir
git remote set-url origin git@github.com:mustafatopall/spring-boot-backend-application.git
```

## Önerilen Yöntem

**SSH kullanın!** Daha güvenli ve kolay:
- Token girmenize gerek yok
- Daha güvenli
- Bir kez kurulum, sürekli kullanım

## Token vs SSH Karşılaştırma

| Özellik | PAT | SSH |
|---------|-----|-----|
| Kurulum | Kolay | Orta |
| Güvenlik | İyi | Çok İyi |
| Kullanım | Her push'ta token | Token yok |
| Süre | 90 gün (yenilenmeli) | Süresiz |
| Önerilen | Geçici | Kalıcı |

## Hızlı Komutlar

```bash
# Git durumunu kontrol et
git status

# Dosyaları ekle
git add .

# Commit yap
git commit -m "Initial commit: Spring Boot backend API project"

# Remote'u kontrol et
git remote -v

# Push yap (PAT veya SSH ile)
git push -u origin main
```

## Sonuç

1. ✅ **PAT Oluştur** (GitHub Settings > Developer settings > Personal access tokens)
2. ✅ **Token'ı Kopyala** (bir daha gösterilmeyecek!)
3. ✅ **Push Yap** (username: GitHub kullanıcı adı, password: token)
4. ✅ **Veya SSH Kullan** (daha kalıcı çözüm)

Hangi yöntemi kullanmak istersiniz? PAT daha hızlı, SSH daha kalıcı!

