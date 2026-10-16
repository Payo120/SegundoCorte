# Evidencias y Matriz de Pruebas – Papelería Campus

## Búsquedas y Filtros (B1 - B7)
- **B1 (Texto: CUADERNO):** Muestra P001 y P002. Contador: 2[cite: 3]. (Exitoso)
- **B2 (Texto: T00):** Muestra T001 a T004. Contador: 4[cite: 3]. (Exitoso)
- **B3 (Categoría: Papelería):** Muestra P001 a P004. Contador: 4[cite: 3]. (Exitoso)
- **B4 (Solo con existencias):** Muestra 9 productos (excluye P004, T004, A003). (Exitoso)
- **B5 (Texto USB + Tecnología + Con existencias):** Muestra T001 y T003. Contador: 2[cite: 3]. (Exitoso)
- **B6 (Texto inexistente ZZZ999):** Oculta filas, muestra contador 0 y aviso de "Sin coincidencias". (Exitoso)
- **B7 (Limpiar filtros tras B5):** Restablece todos los controles y muestra las 12 filas originales[cite: 3]. (Exitoso)

## Operaciones CRUD y Sincronización (C5)
- **Crear:** Inserción de `X001`, `Producto de prueba`, `Accesorios`, `10.00`, stock `1`. Se actualiza el TableView y persiste al reiniciar[cite: 6]. (Exitoso)
- **Actualizar:** Modificación del producto creado a precio `15.50` y stock `3`. Se refleja en la tabla y base de datos[cite: 6]. (Exitoso)
- **Eliminar:** Solicitud de confirmación; cancelar conserva el registro, aceptar lo elimina exclusivamente de la base y la interfaz[cite: 6]. (Exitoso)

## Validaciones y Excepciones (V1-V10 y E1-E4)
- **V1-V8:** Rechazo correcto de códigos vacíos, nombres cortos/largos, categorías no válidas, precios con más de 2 decimales o negativos, y stocks fuera de rango[cite: 4]. (Exitoso)
- **V9:** Rechazo de códigos duplicados tras normalizar[cite: 4]. (Exitoso)
- **V10:** Bloqueo de actualización o eliminación si no hay una fila seleccionada[cite: 4]. (Exitoso)
- **E1-E4:** Manejo de excepciones de conexión, restricciones SQL de integridad, control de 0 filas afectadas y registro de SQLState en consola[cite: 4]. (Exitoso)
