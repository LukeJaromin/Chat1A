#Opis funkcjonalności:
##Rejestracja klienta
W konsoli należy wpisać nazwę użytkownika. Powinna składać się tylko z liter i cyfr i być jednoczłonowa. 

Na ten moment nie działa sprawdzanie, czy nazwa użytkownika jest zduplikowana. Można to zaimplementować tak samo jak przesyłanie wiadomości prywatnych, tzn. sprawdzanie, czy użytkownik jest w mapie chatWorkers.

##Wiadomości prywatne
Wiadomość powinna zaczynać się od ciągu znaków ```@nazwaUżytkownika```, np. ```@jasio```
Taka wiadomość zostanie przesłana tylko do podanego użytkownika. W razie braku określonego użytkownika zostanie zwrócony komunikat do nadawcy. 

##Wiadomości publiczne
Wysyłane do wszystkich użytkowników po wpisaniu tekstu w konsoli, którego pattern nie pasuje do innych czynności.

##Tworzenie kanału
Wpisanie w konsoli ```#CREATE_nazwa```
Pozostała część komunikatu zostanie zignorowana. Nazwa czatu musi być jednoczłonowa i zawierać wyłącznie litery i cyfry.
Twórca otrzyma komunikat o wyniku operacji. Nazwy kanałów nie mogą się duplikować. 
Twórca kanału zostaje automatycznie dodany do użytkowników.

##Dodawanie użytkownika do kanału
Wpisanie w konsoli ```#ADD_użytkownik_kanał```, np. ```#ADD_jasio_pizza```
Tylko członkowie danego kanału mogą dodawać innych członków.

##Opuszczanie kanału
Wpisanie w konsoli ```#LEAVE_nazwaKanału```
Działa tylko dla członków danego kanału. 
Nie można usunąć innego użytkownika z kanału. 

##Usuwanie kanału
Kanał zostaje usunięty automatycznie po opuszczeniu go przez wszystkich użytkowników.

##Wysyłanie wiadomości na kanał
Wpisanie w konsoli ```#nazwaKanału wiadomość```

#Wiadomości
Wszystkie wiadomości przesyłane są w formacie **Message**. 

Readery służą do odczytu wiadomości z socketów i ich dalszym przesyłaniu lub przetwarzaniu na formę tekstową.
Jest też wyodrębniony Reader do odczytu tekstu z konsoli i jej przetwarzania na Message przy pomocy klasy **MessageBuilder**, a następnie jej przesyłanie przy pomocy Writera.

Do obsługi Writerów i Readerów utworzony został zmodyfikowany typ ObjectOutputStream i ObjectInputStream, gdyż był problem z nagłówkami (więcej strumieni z jednego gniazda) i ich weryfikacja musiała zostać wyłączona. 
Wiadomości podzielone są na typy w zależności od rodzaju działania, jakie mają wywołać. Typy zdefiniowane są w klasie **Action**.

ChatWorker po otrzymaniu wiadomości podejmuje odpowiednie działania na podstawie przesłanego w Message typu Action.

#Przesyłanie plików
Przesyłanie plików odbywa się z wykorzystaniem obiektu Message. 

Aby wysłać plik należy podać ścieżkę do pliku na dysku w formacie: 
```%SEND_username_ścieżka``` 
np.: ```%SEND_Adam_src/main/resources/files/users/MyFile.txt```
Powoduje to zapisanie pliku na serwerze, a odbiorca otrzymuje wiadomość typu:
Ewa sent you a file: MyFile.txt You can download it by providing directory and filename in the following format: ```%DOWNLOAD_MyFile.txt_src/main/resources/files/downloaded```
Skopiowanie podanego formatu (lub podmiana ścieżki na własną) i wklejenie w konsoli wywołuje wysłanie wiadomości do serwera i pobranie pliku do wskazanego katalogu. 
Na ten moment nie działa zapis w trakcie działania programu - pliki są dostępne dopiero po zamknięciu jednego z klientów.

#Zapis i odczyt historii rozmowy
Zapis do pliku ```history.txt``` następuje podczas trwania rozmowy. Tekst jest formatowany do postaci ```nadawca | odbiorca | kanał | wiadomość```.
Odczyt zapisanej historii następuje na trzech poziomach: 
odczyt historii rozmów na kanale ogólnym za pomocą komendy ```HISTORY```.
Odczyt historii rozmów prywatnych poprzez wpisanie w konsoli ```@HISTORY_odbiorca```
Odczyt historii rozmów z danego kanału komendą ```#HISTORY_nazwaKanału```. Użytkownik otrzyma historię rozmów z kanału tylko jeśli był jego członkiem.
Odczyt historii z kanału jest możliwy tylko w trakcie trwanie sesji w której został on utworzony. Dostęp do historii rozmów publicznych i prywatnych jest możliwy również w kolejnych sesjach.
Na ten moment do zaimplementowania jest dodanie godziny i daty konkretnej wiadomości. Do rozważenia jest ewentualne kasowanie rozmów publicznych po każdej sesji.
