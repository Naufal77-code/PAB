# Coroutine Demo (M17 - Coroutines and Asynchronous Programming)

Aplikasi Android berbasis Jetpack Compose dengan arsitektur MVVM yang mendemonstrasikan pemrograman asinkron menggunakan Kotlin Coroutines (`viewModelScope`, `coroutineScope`, `async/await`, `withContext`, pembatalan `Job`, dan `StateFlow`).

---

## Fitur & Modifikasi yang Diterapkan
1. **Pemuatan Data Konkuren (Latihan 1):** Menjalankan 3 pemanggilan data secara bersamaan (`getTasks()` 2 detik, `getAnnouncement()` 1 detik, dan `getCourseCount()` 1,5 detik) menggunakan `async` di dalam `coroutineScope`.
2. **Penanganan Error & Tombol Retry (Latihan 2):** Menampilkan pesan kesalahan spesifik saat `IOException` terjadi disertai tombol **Retry** untuk memuat ulang data secara langsung.
3. **Pembatalan Coroutine (`Cancel`):** Membatalkan `Job` yang sedang berjalan tanpa merusak *state* UI.
4. **Pengujian Responsivitas UI (`Tap me` & `Block UI`):** Membandingkan eksekusi latar belakang non-blocking dengan pemblokiran *main thread* (`Thread.sleep(3000L)`).
5. **Pengurutan Data di Background (Latihan 6):** Tombol **Sort** mengurutkan daftar tugas berdasarkan judul secara asinkron menggunakan `withContext(Dispatchers.Default)`.

---

## Jawaban Analisis Latihan (Bagian 9)

### Latihan 1: Total Waktu Pemuatan dengan 3 `async`
* **Apakah total waktu pemuatan tetap ~2 detik?** Ya, total waktu pemuatan tetap sekitar **2 detik** (bukan 4,5 detik).
* **Penjelasan:** Ketiga fungsi (`getTasks()`, `getAnnouncement()`, dan `getCourseCount()`) dipanggil menggunakan `async` di dalam satu `coroutineScope`, sehingga ketiganya dieksekusi secara konkuren (bersamaan). Total durasi tunggu hanya bergantung pada pekerjaan yang paling lama selesai, yaitu `getTasks()` selama 2 detik.

### Latihan 3: Mengganti `IOException` menjadi `Exception` Lalu Menekan `Cancel`
* **Pesan yang muncul:** `"Failed to load: StandaloneCoroutine was cancelled"` (bukan `"Loading cancelled"`).
* **Penjelasan:** Pemanggilan `loadJob?.cancel()` membuat fungsi `delay()` melempar `CancellationException`, yang merupakan subkelas dari `Exception`. Karena blok `catch (e: Exception)` terlalu luas, blok tersebut ikut menangkap pembatalan coroutine dan menimpa `errorMessage` dengan pesan dari `CancellationException`.

### Latihan 4: `Thread.sleep(2000L)` Dengan dan Tanpa `withContext(Dispatchers.IO)`
* **Dengan `withContext(Dispatchers.IO)`:** **UI tidak macet.** Pemblokiran `Thread.sleep()` terjadi pada thread latar belakang milik `Dispatchers.IO`, sehingga *main thread* tetap bebas merender animasi loading dan merespons tombol `Tap me`.
* **Tanpa `withContext(Dispatchers.IO)`:** **UI macet (*freeze*) selama 2 detik.** Secara bawaan `viewModelScope` berjalan di `Dispatchers.Main`, sehingga `Thread.sleep(2000L)` memblokir *main thread* secara langsung dan menghentikan seluruh interaksi layar.

### Latihan 5: Pertanyaan Konsep Coroutines
* **(a) Perbedaan `launch` dan `async`:** `launch` mengembalikan `Job` dan digunakan untuk menjalankan tugas asinkron yang tidak mengembalikan nilai hasil (*fire-and-forget*). Sebaliknya, `async` mengembalikan `Deferred<T>` dan digunakan ketika hasil komputasi asinkron perlu diambil menggunakan `.await()`.
* **(b) Alasan `GlobalScope` dihindari:** `GlobalScope` hidup selama seluruh siklus hidup aplikasi dan tidak otomatis dibatalkan saat komponen UI (`Activity`/`ViewModel`) dihancurkan, sehingga berisiko menyebabkan *memory leak* dan pemborosan sumber daya.
* **(c) Dampak kegagalan `getTasks()` terhadap `getAnnouncement()`:** Sesuai prinsip *structured concurrency*, ketika `getTasks()` gagal dan melempar pengecualian di dalam `coroutineScope`, scope induk akan segera membatalkan semua coroutine saudara (*sibling*) di dalamnya termasuk `getAnnouncement()`sebelum meneruskan pengecualian tersebut ke pemanggilnya.
