# Relación One-to-Many entre Faculty y Student

La relación One-to-Many se implementa usando dos clases:

- `Faculty`: Entidad principal que representa la facultad (lado "uno")
- `Student`: Entidad secundaria que representa al estudiante (lado "muchos")

## Clase Faculty

### Código Java

```java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "faculties",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_faculty_code",
            columnNames = {"faculty_code"}
        ),
        @UniqueConstraint(
            name = "uk_faculty_name",
            columnNames = {"name"}
        )
    }
)
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "faculty_code",
        length = 10,
        nullable = false
    )
    private String facultyCode;

    @Column(
        name = "name",
        length = 100,
        nullable = false
    )
    private String name;

    @Column(
        name = "max_capacity",
        columnDefinition = "INTEGER DEFAULT 500"
    )
    private Integer maxCapacity;

    @Column(
        name = "foundation_year",
        nullable = false
    )
    private Integer foundationYear;

    @Column(
        name = "annual_budget",
        precision = 12,
        scale = 2
    )
    private BigDecimal annualBudget;

    @Column(
        name = "created_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @OneToMany(
        mappedBy = "faculty",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Student> students;
}
```

### DDL Generado

```sql
CREATE TABLE faculties (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    faculty_code VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    max_capacity INTEGER DEFAULT 500,
    foundation_year INTEGER NOT NULL,
    annual_budget NUMERIC(12,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_faculty_code UNIQUE (faculty_code),
    CONSTRAINT uk_faculty_name UNIQUE (name)
);
```

### Explicación del Mapeo

1. **Atributos básicos**:
    - `id`: Se convierte en `bigint` con autoincremento
    - `facultyCode`: Se convierte en `varchar(10)` con restricción `not null`
    - `name`: Se convierte en `varchar(100)` con restricción `not null`
    - `maxCapacity`: Se convierte en `INTEGER` con valor por defecto 500
    - `foundationYear`: Se convierte en `INTEGER` con restricción `not null`
    - `annualBudget`: Se convierte en `numeric(12,2)` permitiendo decimales
    - `createdAt`: Se convierte en `TIMESTAMP` con valor por defecto timestamp actual

2. **Relación One-to-Many**:
    - `@OneToMany`: No genera columnas en el DDL
    - La relación se maneja desde el lado "muchos" (Student)

## Clase Student

### Código Java

```java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "students",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_student_enrollment",
            columnNames = {"enrollment_number"}
        ),
        @UniqueConstraint(
            name = "uk_student_email",
            columnNames = {"email"}
        )
    }
)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "enrollment_number",
        length = 20,
        nullable = false
    )
    private String enrollmentNumber;

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
        name = "email",
        length = 100,
        nullable = false
    )
    private String email;

    @Column(
        name = "current_semester",
        columnDefinition = "INTEGER DEFAULT 1"
    )
    private Integer currentSemester;

    @Column(
        name = "gpa",
        precision = 3,
        scale = 2
    )
    private BigDecimal gpa;

    @Column(
        name = "active",
        columnDefinition = "BOOLEAN DEFAULT true"
    )
    private Boolean active;

    @Column(
        name = "admission_date",
        nullable = false
    )
    private LocalDate admissionDate;

    @Column(
        name = "created_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "faculty_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_student_faculty")
    )
    private Faculty faculty;
}
```

### DDL Generado

```sql
CREATE TABLE students (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    enrollment_number VARCHAR(20) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    current_semester INTEGER DEFAULT 1,
    gpa NUMERIC(3,2),
    active BOOLEAN DEFAULT true,
    admission_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    faculty_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_student_enrollment UNIQUE (enrollment_number),
    CONSTRAINT uk_student_email UNIQUE (email),
    CONSTRAINT fk_student_faculty FOREIGN KEY (faculty_id) 
        REFERENCES faculties (id)
);
```

### Explicación del Mapeo

1. **Atributos básicos**:
    - `id`: Se convierte en `bigint` con autoincremento
    - `enrollmentNumber`: Se convierte en `varchar(20)` con restricción `not null`
    - `firstName`, `lastName`: Se convierten en `varchar(50)` con restricción `not null`
    - `email`: Se convierte en `varchar(100)` con restricción `not null`
    - `currentSemester`: Se convierte en `INTEGER` con valor por defecto 1
    - `gpa`: Se convierte en `numeric(3,2)` permitiendo decimales
    - `active`: Se convierte en `BOOLEAN` con valor por defecto true
    - `admissionDate`: Se convierte en `DATE` con restricción `not null`
    - `createdAt`: Se convierte en `TIMESTAMP` con valor por defecto timestamp actual

2. **Relación Many-to-One**:
    - `@ManyToOne`: Genera la columna `faculty_id` en el DDL
    - Se crea una clave foránea con nombre personalizado usando `@ForeignKey`
    - La columna es `not null` asegurando que todo estudiante tenga una facultad

## Observaciones sobre la Relación One-to-Many

1. **Implementación de la relación**:
    - La relación es unidireccional desde Faculty hacia Student
    - Faculty mantiene una colección de Students
    - Student mantiene una referencia a su Faculty

2. **Bidireccionalidad**:
    - La relación es bidireccional
    - `mappedBy = "faculty"` en Faculty indica que Student es el propietario de la relación
    - La navegación es posible en ambas direcciones

3. **Carga Perezosa (Lazy Loading)**:
    - Se utiliza `FetchType.LAZY` en ambos lados de la relación
    - Los estudiantes solo se cargan cuando se accede a la colección
    - La facultad del estudiante se carga solo cuando se accede a ella

4. **Gestión de la persistencia**:
    - `CascadeType.ALL`: Las operaciones en Faculty se propagan a sus Students
    - La clave foránea asegura la integridad referencial
    - Los estudiantes no pueden existir sin una facultad (`nullable = false`)