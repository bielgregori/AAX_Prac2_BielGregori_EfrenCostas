# AAX Pràctica 2 - API REST Borsa de Valors

## 📋 Descripció
API REST per gestionar borses de valors, empreses i usuaris amb actualització automàtica de preus d'accions.

## 🚀 Com configurar el projecte (IMPORTANT!)

### 1. Clonar el repositori
```bash
git clone https://github.com/bielgregori/AAX_Prac2_BielGregori_EfrenCostas.git
cd AAX_Prac2_BielGregori_EfrenCostas
```

### 2. Compilar el projecte amb Maven
**MOLT IMPORTANT:** Després de cada `git pull`, has de recompilar el projecte!

```bash
cd API
mvn clean package
```

Això generarà el fitxer `API/target/restwsexample.war`

### 3. Desplegar al Tomcat
1. Copia el fitxer `API/target/restwsexample.war` a la carpeta `webapps` del teu Tomcat
2. Reinicia Tomcat
3. Accedeix a: `http://localhost:8080/restwsexample/`

## 🔄 Workflow de desenvolupament

### Abans de començar a treballar:
```bash
git pull origin main
cd API
mvn clean package
```

### Després de fer canvis:
```bash
git add .
git commit -m "Descripció dels canvis"
git pull origin main  # Per si el company ha fet push
git push origin main
```

## ⚙️ Funcionalitats

### 📊 Actualització automàtica de preus
- Els preus de les accions s'actualitzen automàticament **cada 5 segons**
- Variació: ±5 unitats per actualització
- Rang de preus: 1-100

### 🌐 Endpoints disponibles

#### Usuaris
- `GET /api/users` - Obtenir tots els usuaris
- `GET /api/users/{id}` - Obtenir usuari per ID
- `POST /api/users` - Crear nou usuari
- `PUT /api/users/{id}` - Actualitzar usuari
- `DELETE /api/users/{id}` - Eliminar usuari

#### Borses
- `GET /api/bolsa` - Obtenir totes les borses
- `GET /api/bolsa/{id}` - Obtenir borsa per ID
- `POST /api/bolsa` - Crear nova borsa
- `DELETE /api/bolsa/{id}` - Eliminar borsa

#### Empreses
- `GET /api/empresa` - Obtenir totes les empreses
- `GET /api/empresa/{id}` - Obtenir empresa per ID
- `POST /api/empresa` - Crear nova empresa
- `PUT /api/empresa/{id}` - Actualitzar empresa
- `DELETE /api/empresa/{id}` - Eliminar empresa

## 🛠️ Tecnologies utilitzades
- Jakarta EE (JAX-RS)
- JPA / Hibernate
- H2 Database (en memòria)
- Maven
- Apache Tomcat 10

## ⚠️ Notes importants

1. **Base de dades en memòria:** Les dades es perden quan es reinicia el servidor
2. **No pujar binaris:** La carpeta `target/` està al `.gitignore` - cada desenvolupador ha de compilar localment
3. **StockPriceUpdater:** S'activa automàticament quan es crida qualsevol endpoint d'empreses

## 👥 Autors
- Biel Gregori
- Efren Costas
