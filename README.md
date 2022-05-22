### Cuprins

* [Descriere generala](#descriere-generala )
* [Tehnologii folosite](#tehnologii-folosite)
* [Comenzi](#comenzi)
* [Developeri](#developeri)

---

### Descriere generala
Această aplicație are scopul de a pune in contact clientii cu frizerii locali.

---

### Tehnologii folosite
* [Java 8](https://www.oracle.com/java/technologies/javase-downloads.html)    (backend)
* [JavaFX](https://openjfx.io/openjfx-docs/)    (frontend)
* [Maven](https://maven.apache.org/)
* [Nitrite Java](https://www.dizitart.org/nitrite-database.html) (as Database)

---
### Comenzi
* Build :
<pre>
mvn clean install
</pre>

* Run
<pre>
mvn javafx:run
</pre>

---
Encrypting the passwords is done via the following 2 Java functions,:
<pre>
    private static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }
</pre>

<pre>
 public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("users_database.db").toFile())
                .openOrCreate("test", "test");

        userRepository = database.getRepository(User.class);
    }
</pre>
---
## Developeri
### Tomici Bogdan

### Andreascec Darius