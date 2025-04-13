🚲 Projekt symuluje system wypożyczalni rowerów, zbudowany wyłącznie po stronie backendowej. Aplikacja została zaimplementowana z wykorzystaniem różnych technologii bazodanowych i komunikacyjnych – każda wersja znajduje się na osobnym branchu, co umożliwia porównanie ich wydajności, architektury oraz stylu integracji.

🔧 Technologie użyte w różnych wersjach projektu  
Każdy branch zawiera pełną wersję systemu, opartą na innej technologii:  

MongoDB – dokumentowa baza danych do przechowywania danych o rowerach, użytkownikach i wypożyczeniach.  

Redis – wersja wykorzystująca przechowywanie danych w pamięci z dodatkowymi mechanizmami TTL i cache.  

Cassandra – rozproszona baza danych do pracy z dużą ilością danych historycznych.  

Apache Kafka – wersja systemu z komunikacją opartą na komunikatach i zdarzeniach.  

Hibernate (JPA) – wersja relacyjna, wykorzystująca klasyczny model ORM.  

🎯 Cel projektu  
Porównanie różnych podejść do tworzenia systemów backendowych.  

Analiza wydajności i skalowalności każdej z technologii.  
