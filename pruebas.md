# Evidencias y Matriz de Pruebas – Papelería Campus

## 1. Listado Inicial y Carga desde PostgreSQL
La aplicación establece conexión real con PostgreSQL mediante JDBC y carga correctamente los 12 registros obligatorios en el TableView, mostrando las 6 columnas solicitadas y el contador de registros en la interfaz gráfica.

<img width="1049" height="650" alt="Captura de pantalla 2026-10-16 083929" src="https://github.com/user-attachments/assets/d5291499-7c99-4a89-b4d5-dfca24119000" />


---

## 2. Búsquedas y Filtros (B1 - B7)
- **B1 (Texto: CUADERNO):** Filtra y muestra correctamente los productos P001 y P002. Contador actualizado a 2. (Exitoso)
- **B2 (Texto: T00):** Filtra y muestra los productos T001 a T004. Contador actualizado a 4. (Exitoso)
- **B3 (Categoría: Papelería):** Muestra de forma exclusiva los 4 registros de la categoría Papelería. (Exitoso)
- **B4 (Solo con existencias):** Filtra y oculta los productos con stock en 0 (P004, T004, A003), mostrando 9 productos disponibles. (Exitoso)
- **B5 (Filtro combinado - Texto USB + Tecnología + Con existencias):** Devuelve de manera precisa T001 y T003. (Exitoso)
- **B6 (Texto inexistente ZZZ999):** Oculta todas las filas de la tabla, muestra el contador en 0 y despliega el aviso de "Sin coincidencias". (Exitoso)
- **B7 (Limpiar filtros):** Restablece los campos de búsqueda, categorías y checkboxes, restaurando la visualización de las 12 filas originales. (Exitoso)

---

## 3. Operaciones CRUD y Sincronización (C5)
- **Crear (Insert):** Inserción exitosa del producto con código `X001`, nombre "Producto de prueba", categoría "Accesorios", precio `10.00` y stock `1`. Se actualiza el TableView y persiste correctamente en la base de datos. (Exitoso)
- **Actualizar (Update):** Modificación del producto recién creado a precio `15.50` y stock `3`. Los cambios se reflejan de forma inmediata en la interfaz y en PostgreSQL. (Exitoso)
- **Eliminar (Delete):** Al intentar eliminar, se despliega una alerta de confirmación; al cancelar se conserva el registro, y al aceptar se elimina permanentemente de la base de datos y de la tabla visual. (Exitoso)

---

## 4. Validaciones y Manejo de Excepciones (V1-V10 y E1-E4)
- **V1-V8:** Rechazo adecuado y control visual mediante alertas ante códigos vacíos o mayores a 10 caracteres, nombres fuera del rango de 3 a 80, categorías inválidas, precios con más de 2 decimales o negativos, y stocks fuera de rango entero. (Exitoso)
- **V9 (Códigos duplicados):** Bloqueo y advertencia en pantalla al intentar registrar un código que ya existe en la base de datos tras normalizar a mayúsculas. (Exitoso)
- **V10 (Selección obligatoria):** Restricción que bloquea las acciones de actualización o eliminación si el usuario no ha seleccionado previamente una fila en el TableView. (Exitoso)
- **E1-E4 (Excepciones SQL):** Captura y gestión robusta de errores de conexión, violaciones de restricciones CHECK/UNIQUE y control de ceros filas afectadas, imprimiendo el SQLState correspondiente en la consola del sistema. (Exitoso)
