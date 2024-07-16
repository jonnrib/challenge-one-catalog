package com.catalog.literAlura.main;

import com.catalog.literAlura.model.*;
import com.catalog.literAlura.repository.AuthorRepository;
import com.catalog.literAlura.repository.BookRepository;
import com.catalog.literAlura.service.ConsumeApi;
import com.catalog.literAlura.service.DataConverter;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private ConsumeApi consumeApi = new ConsumeApi();
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private List<Book> books = new ArrayList<>();
    private DataConverter dataConverter = new DataConverter();
    private final String ADDRESS = "https://gutendex.com/books?search=";

    public Main(AuthorRepository authorRepository, BookRepository bookRepository){
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void displayMenu(){
        String menu = """
                **********************************************
                
                1 - Search book by title
                2 - List registered books
                3 - List registered authors
                4 - List living authors in a specific year
                5 - List books by language
                6 - Top 10 books
                7 - Search authors by name
                8 - Average downloads per author
                
                0 - Exit
                
                **********************************************
                """;
        var option = -1;
        while (option != 0){
            System.out.println(menu);
            try {
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        searchNewBook();
                        break;
                    case 2:
                        listRegisteredBooks();
                        break;
                    case 3:
                        listRegisteredAuthors();
                        break;
                    case 4:
                        listLivingAuthorsByYear();
                        break;
                    case 5:
                        listBooksByLanguage();
                        break;
                    case 6:
                        listTop10Books();
                        break;
                    case 7:
                        searchAuthorByName();
                        break;
                    case 8:
                        averageDownloadsPerAuthor();
                        break;
                    case 0:
                        System.out.println("Exiting");
                        break;
                    default:
                        System.out.println("\n*** Invalid Option ***\n");
                }
            } catch (Exception e) {
                System.out.println("\n*** Invalid Option ***\n");
                scanner.nextLine();
            }
        }

    }

    private void searchNewBook() {
        System.out.print("\nWhich book would you like to search for? ");
        var userSearch = scanner.nextLine();
        var data = consumeApi.consume(ADDRESS + userSearch.replace(" ", "%20"));
        // System.out.println("API response: " + data);
        if (save(data)) {
            System.out.println("Book saved successfully!");
        } else {
            System.out.println("\n*** Book not found ***\n");
        }
    }

    private boolean save(String data) {
        try {
            BookData bookData = dataConverter.getData(data, BookData.class);
            AuthorData authorData = dataConverter.getData(data, AuthorData.class);

            if (bookData == null || authorData == null) {
                // System.out.println("\n\n*** Error in data conversion ***\n\n");
                return false;
            }

            Author author = new Author(authorData);
            if (!authorRepository.existsByName(author.getName())) {
                author = authorRepository.save(author);
            } else {
                author = authorRepository.findByName(author.getName());
            }

            Book book = new Book(bookData);
            book.setAuthor(author);
            if (!bookRepository.existsByName(book.getName())) {
                bookRepository.save(book);
            }

            System.out.println("Book saved: " + book);
            return true;
        } catch (Exception e) {
            System.out.println("\n\n*** Error saving the book ***\n\n");
            e.printStackTrace();
            return false;
        }
    }

    private void listRegisteredBooks() {
        var booksDb = bookRepository.findAll();
        if(!booksDb.isEmpty()){
            System.out.println("\nBooks registered in the database: ");
            booksDb.forEach(System.out::println);
        }else{
            System.out.println("\nNo books found in the database!");
        }
    }

    private void listRegisteredAuthors() {
        var authorsDb = authorRepository.findAll();
        if(!authorsDb.isEmpty()){
            System.out.println("\nAuthors registered in the database:");
            authorsDb.forEach(System.out::println);
        }else{
            System.out.println("\nNo authors found in the database!");
        }
    }

    private void listLivingAuthorsByYear() {
        System.out.print("\nWhich year would you like to search for? ");
        var selectedYear = scanner.nextInt();
        scanner.nextLine();
        var authorsFound = authorRepository.findLivingAuthorsByYear(selectedYear);
        if(!authorsFound.isEmpty()){
            System.out.println("\n\nAuthors living in the year: " + selectedYear);
            authorsFound.forEach(System.out::println);
        }else {
            System.out.println("\nNo authors found for this date!");
        }
    }

    private void listBooksByLanguage() {
        var registeredLanguages = bookRepository.searchLanguages();
        System.out.println("\nAvailable languages:");
        registeredLanguages.forEach(System.out::println);
        System.out.println("\nSelect one of the languages:\n");
        var selectedLanguage = scanner.nextLine();
        bookRepository.findByLanguage(selectedLanguage).forEach(System.out::println);
    }

    private void listTop10Books() {
        var top10 = bookRepository.findTop10ByOrderByDownloadCountDesc();
        top10.forEach(System.out::println);
    }

    private void searchAuthorByName() {
        System.out.print("What is the name of the author? ");
        var search = scanner.nextLine();
        var authors = authorRepository.findByNameContainingIgnoreCase(search);
        if (!authors.isEmpty()){
            authors.forEach(System.out::println);
        }else{
            System.out.println("*** Author not found! ***");
        }
    }

    private void averageDownloadsPerAuthor() {
        System.out.print("Which author would you like to search for? ");
        var search = scanner.nextLine();
        var books = bookRepository.findBooksByAuthor(search);
        DoubleSummaryStatistics stats = books.stream()
                .mapToDouble(Book::getDownloadCount)
                .summaryStatistics();
        System.out.println("Average Downloads: "+ stats.getAverage());
    }
}
