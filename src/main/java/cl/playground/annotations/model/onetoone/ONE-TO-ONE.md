# Relación One-to-One entre User y UserProfile

La relación One-to-One se implementa usando dos clases:

- `User`: Entidad principal que representa al usuario
- `UserProfile`: Entidad que representa el perfil detallado del usuario

## Clase User

### Código Java

```java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_username",
            columnNames = {"username"}
        ),
        @UniqueConstraint(
            name = "uk_user_email",
            columnNames = {"email"}
        )
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "username",
        length = 50,
        nullable = false
    )
    private String username;

    @Column(
        name = "email",
        length = 100,
        nullable = false
    )
    private String email;

    @Column(
        name = "password_hash",
        length = 60,
        nullable = false
    )
    private String passwordHash;

    @Column(
        name = "account_locked",
        columnDefinition = "BOOLEAN DEFAULT false"
    )
    private Boolean accountLocked;

    @Column(
        name = "last_login",
        columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime lastLogin;

    @Column(
        name = "created_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @OneToOne(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        optional = false
    )
    private UserProfile profile;
}
```

### DDL Generado

```sql
CREATE TABLE users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    account_locked BOOLEAN DEFAULT false,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_user_username UNIQUE (username),
    CONSTRAINT uk_user_email UNIQUE (email)
);
```

### Explicación del Mapeo

1. **Atributos básicos**:
    - `id`: Se convierte en `bigint` con autoincremento
    - `username`: Se convierte en `varchar(50)` con restricción `not null`
    - `email`: Se convierte en `varchar(100)` con restricción `not null`
    - `passwordHash`: Se convierte en `varchar(60)` para almacenar hash BCrypt
    - `accountLocked`: Se convierte en `BOOLEAN` con valor por defecto false
    - `lastLogin`: Se convierte en `TIMESTAMP` nullable
    - `createdAt`: Se convierte en `TIMESTAMP` con valor por defecto timestamp actual

2. **Relación One-to-One**:
    - `@OneToOne`: No genera columnas en el DDL
    - `mappedBy = "user"` indica que UserProfile es el propietario de la relación
    - `optional = false` indica que el perfil es obligatorio

## Clase UserProfile

### Código Java

```java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true,
        foreignKey = @ForeignKey(name = "fk_profile_user")
    )
    private User user;

    @Column(
        name = "first_name",
        length = 50,
        nullable = false
    )
    private String firstName;

    @Column(
        name = "last_name",
        length = 50,
        nullable = false
    )
    private String lastName;

    @Column(
        name = "date_of_birth"
    )
    private LocalDate dateOfBirth;

    @Column(
        name = "phone_number",
        length = 20
    )
    private String phoneNumber;

    @Column(
        name = "address",
        length = 200
    )
    private String address;

    @Column(
        name = "bio",
        columnDefinition = "TEXT"
    )
    private String bio;

    @Column(
        name = "profile_picture_url",
        length = 255
    )
    private String profilePictureUrl;

    @Column(
        name = "preferences",
        columnDefinition = "jsonb"
    )
    private String preferences;

    @Column(
        name = "created_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @Column(
        name = "updated_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime updatedAt;
}
```

### DDL Generado

```sql
CREATE TABLE user_profiles (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    user_id BIGINT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    phone_number VARCHAR(20),
    address VARCHAR(200),
    bio TEXT,
    profile_picture_url VARCHAR(255),
    preferences jsonb,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_profile_user UNIQUE (user_id),
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) 
        REFERENCES users (id)
);
```

### Explicación del Mapeo

1. **Atributos básicos**:
    - `id`: Se convierte en `bigint` con autoincremento
    - `firstName`, `lastName`: Se convierten en `varchar(50)` con restricción `not null`
    - `dateOfBirth`: Se convierte en `DATE` nullable
    - `phoneNumber`: Se convierte en `varchar(20)` nullable
    - `address`: Se convierte en `varchar(200)` nullable
    - `bio`: Se convierte en `TEXT` para contenido largo
    - `profilePictureUrl`: Se convierte en `varchar(255)` nullable
    - `preferences`: Se convierte en `jsonb` para almacenar JSON
    - `createdAt`, `updatedAt`: Se convierten en `TIMESTAMP` con valor por defecto

2. **Relación One-to-One**:
    - `@OneToOne` con `@JoinColumn` genera la columna `user_id`
    - `unique = true` en `@JoinColumn` asegura la relación uno a uno
    - Se crea una clave foránea con nombre personalizado

## Observaciones sobre la Relación One-to-One

1. **Implementación de la relación**:
    - La relación es bidireccional
    - UserProfile es el propietario de la relación (tiene la clave foránea)
    - La unicidad se garantiza tanto por `@OneToOne` como por la restricción de base de datos

2. **Carga Perezosa (Lazy Loading)**:
    - Se utiliza `FetchType.LAZY` en ambos lados de la relación
    - Los datos relacionados solo se cargan cuando se acceden

3. **Gestión de la persistencia**:
    - `CascadeType.ALL`: Las operaciones en User se propagan a UserProfile
    - `optional = false`: Asegura que cada usuario tenga un perfil
    - La clave foránea con `unique = true` previene perfiles duplicados

4. **Consideraciones de diseño**:
    - Se separan los datos esenciales (User) de los datos extensos (UserProfile)
    - User contiene información de autenticación
    - UserProfile contiene información descriptiva y preferencias
    - Se usa `jsonb` para almacenar preferencias flexibles