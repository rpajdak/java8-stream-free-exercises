package pl.klolo.workshops.logic;

import pl.klolo.workshops.domain.Currency;
import pl.klolo.workshops.domain.*;
import pl.klolo.workshops.mock.HoldingMockGenerator;
import pl.klolo.workshops.mock.UserMockGenerator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.*;

class WorkShop {

    /**
     * Lista holdingów wczytana z mocka.
     */
    private final List<Holding> holdings;

    // Predykat określający czy użytkownik jest kobietą
    private final Predicate<User> isWoman = user -> user.getSex().equals(Sex.WOMAN);


    // Predykat określający czy użytkownik jest mężczyzną
    private final Predicate<User> isMan = user -> user.getSex().equals(Sex.MAN);


    // Predykat określający czy użytkownik nie jest kobietą
    private final Predicate<User> isNoWoman = user -> !user.getSex().equals(Sex.WOMAN);

    WorkShop() {
        final HoldingMockGenerator holdingMockGenerator = new HoldingMockGenerator();
        holdings = holdingMockGenerator.generate();
    }

    /**
     * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma.
     */
    long getHoldingsWhereAreCompanies() {
        int holdingWithMoreThenOneCompanies = 0;
        for (Holding holding : holdings) {
            if (holding.getCompanies().size() > 1) {
                holdingWithMoreThenOneCompanies++;
            }
        }
        return holdingWithMoreThenOneCompanies;
    }

    /**
     * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma. Napisz to za pomocą strumieni.
     */
    long getHoldingsWhereAreCompaniesAsStream() {
        return holdings.stream()
                .filter(holding -> holding.getCompanies().size() > 1).count();
    }

    /**
     * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy.
     */
    List<String> getHoldingNames() {
        List<String> names = new ArrayList<>();

        for (Holding holding : holdings) {
            names.add(holding.getName().toLowerCase());
        }
        return names;
    }

    /**
     * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy. Napisz to za pomocą strumieni.
     */
    List<String> getHoldingNamesAsStream() {
        return holdings.stream()
                .map(holding -> holding.getName().toLowerCase())
                .collect(Collectors.toList());
    }

    /**
     * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane. String ma postać: (Coca-Cola, Nestle, Pepsico)
     */
    String getHoldingNamesAsString() {
        List<String> names = holdings.stream()
                .map(Holding::getName)
                .collect(Collectors.toList());
        int numberOfNames = names.size();
        int counter = 0;
        StringBuilder result = new StringBuilder();
        names.sort(String::compareTo);
        result.append("(");
        for (String name : names) {
            result.append(name);
            counter++;
            if (counter < numberOfNames) {
                result.append(", ");
            }
        }
        result.append(")");
        return result.toString();
    }

    /**
     * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane. String ma postać: (Coca-Cola, Nestle, Pepsico). Napisz to za pomocą strumieni.
     */
    String getHoldingNamesAsStringAsStream() {
        return holdings.stream()
                .map(Holding::getName)
                .sorted()
                .collect(Collectors.joining(", ", "(", ")"));
    }

    /**
     * Zwraca liczbę firm we wszystkich holdingach.
     */
    long getCompaniesAmount() {
        int result = 0;
        for (Holding holding : holdings) {
            result = result + holding.getCompanies().size();
        }

        return result;
    }

    /**
     * Zwraca liczbę firm we wszystkich holdingach. Napisz to za pomocą strumieni.
     */
    long getCompaniesAmountAsStream() {
        return holdings.stream()
                .map(holding -> holding.getCompanies().size())
                .reduce(0, Integer::sum);
    }

    /**
     * Zwraca liczbę wszystkich pracowników we wszystkich firmach.
     */
    long getAllUserAmount() {
        int result = 0;
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                result += company.getUsers().size();
            }
        }
        return result;
    }

    /**
     * Zwraca liczbę wszystkich pracowników we wszystkich firmach. Napisz to za pomocą strumieni.
     */
    long getAllUserAmountAsStream() {
        return holdings.stream()
                .flatMap(holding -> holding.getCompanies().stream())
                .mapToInt(company -> company.getUsers().size())
//                .reduce(0, (int1, int2) -> int1 + int2);
                .sum();
    }

    /**
     * Zwraca listę wszystkich nazw firm w formie listy.
     */
    List<String> getAllCompaniesNames() {

        List<String> names = new ArrayList<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                names.add(company.getName());
            }
        }
        return names;
    }


    /**
     * Zwraca listę wszystkich nazw firm w formie listy. Tworzenie strumienia firm umieść w osobnej metodzie którą później będziesz wykorzystywać. Napisz to za
     * pomocą strumieni.
     */
    List<String> getAllCompaniesNamesAsStream() {
        return getCompanyStream()
                .map(Company::getName)
                .collect(Collectors.toList());
    }


    /**
     * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList.
     */
    LinkedList<String> getAllCompaniesNamesAsLinkedList() {

        LinkedList<String> names = new LinkedList<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                names.add(company.getName());
            }
        }
        return names;
    }

    /**
     * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList. Obiektów nie przepisujemy po zakończeniu działania strumienia. Napisz to za
     * pomocą strumieni.
     */
    LinkedList<String> getAllCompaniesNamesAsLinkedListAsStream() {
        return getCompanyStream()
                .map(Company::getName)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+"
     */
    String getAllCompaniesNamesAsString() {

        StringBuilder result = new StringBuilder();
        long numberOfCompanies = holdings.stream().mapToLong(holding -> holding.getCompanies().size())
                .sum();
        long counter = 0;
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                result.append(company.getName());
                counter++;
                if (counter < numberOfCompanies) {
                    result.append("+");
                }
            }
        }
        return result.toString();
    }

    /**
     * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+" Napisz to za pomocą strumieni.
     */
    String getAllCompaniesNamesAsStringAsStream() {
        return getCompanyStream()
                .map(Company::getName)
                .collect(Collectors.joining("+"));
    }

    /**
     * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+". Używamy collect i StringBuilder. Napisz to za pomocą
     * strumieni.
     * <p>
     * UWAGA: Zadanie z gwiazdką. Nie używamy zmiennych.
     */
    String getAllCompaniesNamesAsStringUsingStringBuilder() {
        return getCompanyStream()
                .map(Company::getName)
                .collect(Collector.of(StringBuilder::new,
                        (stringBuilder, o) -> stringBuilder.append(o).append("+"),
                        StringBuilder::append,
                        stringBuilder -> stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length())))
                .toString();

    }

    /**
     * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach.
     */
    long getAllUserAccountsAmount() {
        int result = 0;

        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers())
                    result += user.getAccounts().size();
            }
        }
        return result;
    }

    /**
     * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach. Napisz to za pomocą strumieni.
     */
    long getAllUserAccountsAmountAsStream() {
        return getCompanyStream()
                .flatMap(company -> company.getUsers().stream())
                .mapToLong(user -> user.getAccounts().size())
                .sum();
    }

    /**
     * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości występują bez powtórzeń i są posortowane.
     */
    String getAllCurrencies() {
        StringBuilder result = new StringBuilder();
        Set<Currency> currencies = new HashSet<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers())
                    for (Account account : user.getAccounts()) {
                        currencies.add(account.getCurrency());
                    }
            }
        }
        List<Currency> sorted = currencies.stream().sorted(Comparator.comparing(Enum::toString)).
                collect(Collectors.toList());

        int counter = 0;
        int numberOfCurrencies = sorted.size();

        for (Currency currency : sorted) {
            result.append(currency);
            counter++;
            if (counter < numberOfCurrencies) {
                result.append(", ");
            }
        }

        return result.toString();
    }

    /**
     * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości występują bez powtórzeń i są posortowane. Napisz to za pomocą strumieni.
     */
    String getAllCurrenciesAsStream() {
        return getCompanyStream()
                .flatMap(company -> company.getUsers().stream())
                .flatMap(user -> user.getAccounts().stream())
                .map(account -> account.getCurrency().toString())
                .sorted(Comparator.comparing(String::toString))
                .distinct()
                .collect(Collectors.joining(", "));
    }

    /**
     * Metoda zwraca analogiczne dane jak getAllCurrencies, jednak na utworzonym zbiorze nie uruchamiaj metody stream, tylko skorzystaj z  Stream.generate.
     * Wspólny kod wynieś do osobnej metody.
     *
     * @see #getAllCurrencies()
     */
    String getAllCurrenciesUsingGenerate() {
        List<String> currencies = getAllCurrenciesAsList();

        return Stream.generate(currencies.iterator()::next)
                .collect(Collectors.joining(", "));

    }


    /**
     * Zwraca liczbę kobiet we wszystkich firmach.
     */
    long getWomanAmount() {
        int numberOfWomen = 0;
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    if (user.getSex().equals(Sex.WOMAN)) {
                        numberOfWomen++;
                    }
                }
            }
        }
        return numberOfWomen;
    }

    /**
     * Zwraca liczbę kobiet we wszystkich firmach. Powtarzający się fragment kodu tworzący strumień uzytkowników umieść w osobnej metodzie. Predicate określający
     * czy mamy doczynienia z kobietą inech będzie polem statycznym w klasie. Napisz to za pomocą strumieni.
     */
    long getWomanAmountAsStream() {
        return getUserStream()
                .filter(isWoman)
                .count();
    }


    /**
     * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency. Ustaw precyzje na 3 miejsca po przecinku.
     */
    BigDecimal getAccountAmountInPLN(final Account account) {

        return getAmountInCurrency(account);
    }

    private BigDecimal getAmountInCurrency(Account account) {
        return account.getAmount().multiply(BigDecimal.valueOf(account.getCurrency().rate)).setScale(3, RoundingMode.HALF_UP);
    }


    /**
     * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency. Napisz to za pomocą strumieni.
     */
    BigDecimal getAccountAmountInPLNAsStream(final Account account) {
        return Stream.of(account)
                .map(this::getAmountInCurrency)
                .findFirst()
                .get();
    }

    /**
     * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency  i sumuje ją.
     */
    BigDecimal getTotalCashInPLN(final List<Account> accounts) {
        BigDecimal result = new BigDecimal(0);
        for (Account account : accounts) {
            float rate = Currency.valueOf(account.getCurrency().toString()).rate;
            result = result.add(account.getAmount().multiply(BigDecimal.valueOf(rate)));
        }

        return result;
    }

    /**
     * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency  i sumuje ją. Napisz to za pomocą strumieni.
     */
    BigDecimal getTotalCashInPLNAsStream(final List<Account> accounts) {
        return accounts.stream()
                .map(this::getAccountAmountInPLN)
                .reduce(BigDecimal::add).get();
    }

    /**
     * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek.
     */
    Set<String> getUsersForPredicate(final Predicate<User> userPredicate) {
        Set<String> names = new HashSet<>();

        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    if (userPredicate.test(user)) {
                        names.add(user.getFirstName());
                    }
                }
            }
        }
        return names;
    }

    /**
     * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek. Napisz to za pomocą strumieni.
     */
    Set<String> getUsersForPredicateAsStream(final Predicate<User> userPredicate) {
        return getUserStream().filter(userPredicate).map(User::getFirstName).collect(toSet());
    }

    /**
     * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn i zwraca ich imiona w formie listy.
     */
    List<String> getOldWoman(final int age) {
        List<String> names = new ArrayList<>();

        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    if (user.getAge() >= age && user.getSex().equals(Sex.WOMAN)) {
                        names.add(user.getFirstName());
                    }
                }
            }
        }
        return names;
    }

    /**
     * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn i zwraca ich imiona w formie listy. Napisz
     * to za pomocą strumieni.
     */
    List<String> getOldWomanAsStream(final int age) {

        return getUserStream()
                .filter(isWoman)
                .filter(user -> user.getAge() >= age)
                .map(User::getFirstName)
                .collect(Collectors.toList());
    }

    /**
     * Dla każdej firmy uruchamia przekazaną metodę.
     */
    void executeForEachCompany(final Consumer<Company> consumer) {

        getCompanyStream()
                .forEach(consumer);
    }

    /**
     * Wyszukuje najbogatsza kobietę i zwraca ja. Metoda musi uzwględniać to że rachunki są w różnych walutach.
     */
    Optional<User> getRichestWoman() {

        Map<User, BigDecimal> usersAndCash = new HashMap<>();
        Set<User> users = getUserStream().collect(toSet());

        for (User user : users) {
            if (user.getSex().equals(Sex.WOMAN)) {
                BigDecimal amount = new BigDecimal(0);
                for (Account account : user.getAccounts()) {
                    amount = amount.add(getAccountAmountInPLN(account));
                }
                usersAndCash.put(user, amount);
            }
        }

        List<Map.Entry<User, BigDecimal>> list = new LinkedList<>(usersAndCash.entrySet());

        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        User richestWoman = list.get(0).getKey();
        return Optional.ofNullable(richestWoman);
    }

    /**
     * Wyszukuje najbogatsza kobietę i zwraca ja. Metoda musi uzwględniać to że rachunki są w różnych walutach. Napisz to za pomocą strumieni.
     */
    Optional<User> getRichestWomanAsStream() {

        return getUserStream()
                .filter(isWoman)
                .max(Comparator.comparing(this::getUserAmount));


    }

    private BigDecimal getUserAmount(User user) {
        return user.getAccounts().stream()
                .map(this::getAccountAmountInPLN)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia.
     */
    Set<String> getFirstNCompany(final int n) {
        System.out.println(n + " dasdsad");
        int counter = 0;
        Set<String> companyNames = new HashSet<>();
        for (Holding holding : holdings) {

            for (Company company : holding.getCompanies()) {
                if (counter < n) {
                    companyNames.add(company.getName());
                    counter++;
                }

            }
        }
        return companyNames;
    }

    /**
     * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia. Napisz to za pomocą strumieni.
     */
    Set<String> getFirstNCompanyAsStream(final int n) {

        return getCompanyStream()
                .map(Company::getName)
                .limit(n)
                .collect(toSet());
    }

    /**
     * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metdę getAccountStream. Jeżeli nie udało się znaleźć najpopularnijeszego
     * rachunku metoda ma wyrzucić wyjątek IllegalStateException. Pierwsza instrukcja metody to return.
     */
    AccountType getMostPopularAccountType() {

        return getAccountStream()
                .map(Account::getType)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow(IllegalStateException::new);


    }

    /**
     * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metdę getAccountStream. Jeżeli nie udało się znaleźć najpopularnijeszego
     * rachunku metoda ma wyrzucić wyjątek IllegalStateException. Pierwsza instrukcja metody to return. Napisz to za pomocą strumieni.
     */
    AccountType getMostPopularAccountTypeAsStream() {
        return null;
    }


    /**
     * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca wyjątek IllegalArgumentException.
     */
    User getUser(final Predicate<User> predicate) {
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    if (predicate.test(user)) {
                        return user;
                    }
                }
            }
        }

        throw new IllegalArgumentException();
    }

    /**
     * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca wyjątek IllegalArgumentException. Napisz to
     * za pomocą strumieni.
     */
    User getUserAsStream(final Predicate<User> predicate) {
        return getUserStream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników.
     */
    Map<String, List<User>> getUserPerCompany() {
        Map<String, List<User>> companyAndUsersList = new HashMap<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                companyAndUsersList.put(company.getName(), company.getUsers());
            }
        }

        return companyAndUsersList;
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników. Napisz to za pomocą strumieni.
     */
    Map<String, List<User>> getUserPerCompanyAsStream() {

        return getCompanyStream()
                .collect(Collectors.toMap(Company::getName, (Company::getUsers)));

    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako string składający się z imienia i nazwiska. Podpowiedź:
     * Możesz skorzystać z metody entrySet.
     */
    Map<String, List<String>> getUserPerCompanyAsString() {
        Map<String, List<String>> companyAndUsersList = new HashMap<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                List<String> namesList = new ArrayList<>();
                StringBuilder name = new StringBuilder();
                for (User user : company.getUsers()) {
                    name.append(user.getFirstName()).append(" ").append(user.getLastName());
                    namesList.add(name.toString());
                }
                companyAndUsersList.put(company.getName(), namesList);
            }
        }
        return companyAndUsersList;
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako string składający się z imienia i nazwiska. Podpowiedź:
     * Możesz skorzystać z metody entrySet. Napisz to za pomocą strumieni.
     */
    Map<String, List<String>> getUserPerCompanyAsStringAsStream() {

        BiFunction<String, String, String> joinNameAndLastName = (x, y) -> x + " " + y;
        return getCompanyStream().collect(Collectors.toMap(Company::getName, company -> company.getUsers().stream()
                .map(user -> joinNameAndLastName.apply(user.getFirstName(), user.getLastName())).collect(Collectors.toList())));
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty typu T, tworzonych za pomocą przekazanej
     * funkcji.
     */
    <T> Map<String, List<T>> getUserPerCompany(final Function<User, T> converter) {
        return getCompanyStream().collect(Collectors.toMap(Company::getName, company -> company.getUsers().stream().map(converter).collect(toList())));
    }

    /**
     * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty typu T, tworzonych za pomocą przekazanej funkcji.
     * Napisz to za pomocą strumieni.
     */
    <T> Map<String, List<T>> getUserPerCompanyAsStream(final Function<User, T> converter) {
        return getCompanyStream().collect(Collectors.toMap(Company::getName, company -> company.getUsers().stream().map(converter).collect(toList())));

    }

    /**
     * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą. Osoby "innej" płci mają zostać zignorowane. Wartością
     * jest natomiast zbiór nazwisk tych osób.
     */
    Map<Boolean, Set<String>> getUserBySex() {
        Map<Boolean, Set<String>> users = new HashMap<>();
        Set<String> men = new HashSet<>();
        Set<String> women = new HashSet<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    if (user.getSex().equals(Sex.WOMAN)) {
                        women.add(user.getLastName());
                    } else if (user.getSex().equals(Sex.MAN)) {
                        men.add(user.getLastName());
                    }
                }
            }
        }
        users.put(false, women);
        users.put(true, men);
        return users;
    }

    /**
     * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą. Osoby "innej" płci mają zostać zignorowane. Wartością
     * jest natomiast zbiór nazwisk tych osób. Napisz to za pomocą strumieni.
     */
    Map<Boolean, Set<String>> getUserBySexAsStream() {
        final Predicate<User> isManOrWoman = user -> user.getSex().equals(Sex.WOMAN) || user.getSex().equals(Sex.MAN);

        return getUserStream()
                .filter(isManOrWoman)
                .collect(partitioningBy(isMan, mapping(User::getLastName, toSet())));
    }

    /**
     * Zwraca mapę rachunków, gdzie kluczem jesy numer rachunku, a wartością ten rachunek.
     */
    Map<String, Account> createAccountsMap() {
        Map<String, Account> accountMap = new HashMap<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    for (Account account : user.getAccounts()) {
                        accountMap.put(account.getNumber(), account);
                    }
                }
            }
        }


        return accountMap;
    }

    /**
     * Zwraca mapę rachunków, gdzie kluczem jesy numer rachunku, a wartością ten rachunek. Napisz to za pomocą strumieni.
     */
    Map<String, Account> createAccountsMapAsStream() {
        return getUserStream()
                .flatMap(user -> user.getAccounts().stream())
                .collect(Collectors.toMap(Account::getNumber, account -> account));


    }

    /**
     * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń.
     */
    String getUserNames() {
        int counter = 0;
        int maxNames = 5;
        Set<String> names = new HashSet<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    if (user.getSex().equals(Sex.MAN) || user.getSex().equals(Sex.OTHER)) {
                        names.add(user.getFirstName());
                    }
                }
            }
        }
        TreeSet myTreeSet = new TreeSet();

        myTreeSet.addAll(names);
        StringBuilder namesAsString = new StringBuilder();
        for (Object o : myTreeSet) {
            if (counter < 5) {
                namesAsString.append(o.toString());
                counter++;
                if (counter < maxNames) {
                    namesAsString.append(" ");
                }
            }
        }

        System.out.println(namesAsString.toString());
        return namesAsString.toString();
    }


    /**
     * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń. Napisz to za pomocą strumieni.
     */
    String getUserNamesAsStream() {
        String names = getUserStream()
                .filter(isNoWoman)
                .map(User::getFirstName)
                .distinct()
                .sorted()
                .collect(Collectors.joining(" "));
        return names;
    }

    /**
     * zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10.
     */
    Set<User> getUsers() {
        return getUserStream().limit(10).collect(toSet());

    }

    /**
     * zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10. Napisz to za pomocą strumieni.
     */
    Set<User> getUsersAsStream() {

        return getCompanyStream().flatMap(company -> company.getUsers().stream()).limit(10).collect(toSet());

    }

    /**
     * Zwraca użytkownika, który spełnia podany warunek.
     */
    Optional<User> findUser(final Predicate<User> userPredicate) {

        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    if (userPredicate.test(user)) {
                        return Optional.ofNullable(user);
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Zwraca użytkownika, który spełnia podany warunek. Napisz to za pomocą strumieni.
     */
    Optional<User> findUserAsStream(final Predicate<User> userPredicate) {
        return getUserStream().filter(userPredicate).findFirst();
    }

    /**
     * Dla podanego użytkownika zwraca informacje o tym ile ma lat w formie: IMIE NAZWISKO ma lat X. Jeżeli użytkownik nie istnieje to zwraca text: Brak
     * użytkownika.
     * <p>
     * Uwaga: W prawdziwym kodzie nie przekazuj Optionali jako parametrów. Napisz to za pomocą strumieni.
     */
    String getAdultantStatusAsStream(final Optional<User> user) {


        return user.flatMap(u -> getUserStream().filter(u2 -> Objects.equals(u2, u)).findFirst())
                .map(u -> format("%s %s ma lat %d", u.getFirstName(), u.getLastName(), u.getAge()))
                .orElse("Brak użytkownika");

//        return user.flatMap(u -> getUserStream()
//                .filter(u2 -> Objects.equals(u, u2))
//                .findFirst())
//                .map(u -> format("%s %s ma lat %d", u.getFirstName(), u.getLastName(), u.getAge())).orElse("Brak użytkownika");
    }

    /**
     * Metoda wypisuje na ekranie wszystkich użytkowników (imie, nazwisko) posortowanych od z do a. Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred
     * Pasibrzuch, Adam Wojcik
     */
    void showAllUser() {
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    System.out.println(user.getFirstName() + " " + user.getLastName());
                }
            }
        }


    }

    /**
     * Metoda wypisuje na ekranie wszystkich użytkowników (imie, nazwisko) posortowanych od z do a. Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred
     * Pasibrzuch, Adam Wojcik. Napisz to za pomocą strumieni.
     */
    void showAllUserAsStream() {
        getUserStream().map(user -> user.getFirstName() + " " + user.getLastName())
                .forEach(System.out::println);
    }

    /**
     * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu przeliczona na złotówki.
     */
    Map<AccountType, BigDecimal> getMoneyOnAccounts() {
//        Map<AccountType, BigDecimal> accountsList = new HashMap<>();
//        for (Holding holding : holdings) {
//            for (Company company : holding.getCompanies()) {
//                for (User user : company.getUsers()) {
//                    BigDecimal previousValue = new BigDecimal(0);
//                    for (Account account : user.getAccounts()) {
//                        previousValue = accountsList.get(account.getType());
//                        BigDecimal sum = account.getAmount().multiply(BigDecimal.valueOf(Currency.valueOf(account.getCurrency().toString()).rate));
//                        BigDecimal newValue = previousValue.add(sum);
//                        accountsList.put(account.getType(), newValue);
//                    }
//                }
//            }
//        }
//        return accountsList;
        return getAccountStream().collect(Collectors.toMap(Account::getType, account -> account.getAmount()
                .multiply(BigDecimal.valueOf(Currency.valueOf(account.getCurrency().toString()).rate)).round(new MathContext(6, RoundingMode.DOWN)), BigDecimal::add));

    }

    /**
     * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu przeliczona na złotówki. Napisz to za pomocą
     * strumieni. Ustaw precyzje na 0.
     */
    Map<AccountType, BigDecimal> getMoneyOnAccountsAsStream() {
        return getAccountStream().collect(Collectors.toMap(Account::getType, account -> account.getAmount()
                .multiply(BigDecimal.valueOf(Currency.valueOf(account.getCurrency().toString()).rate)).round(new MathContext(6, RoundingMode.DOWN)), BigDecimal::add));
    }

    /**
     * Zwraca sumę kwadratów wieków wszystkich użytkowników.
     */
    int getAgeSquaresSum() {
        int sum = 0;
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    int sqaureUser = user.getAge() * user.getAge();
                    sum += sqaureUser;
                }
            }
        }
        return sum;
    }

    /**
     * Zwraca sumę kwadratów wieków wszystkich użytkowników. Napisz to za pomocą strumieni.
     */
    int getAgeSquaresSumAsStream() {
        return getUserStream()
                .map(user -> user.getAge() * user.getAge())
                .reduce(0, Integer::sum);

    }

    /**
     * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się powtarzać, wszystkie zmienną muszą być
     * final. Jeżeli podano liczbę większą niż liczba użytkowników należy wyrzucić wyjątek (bez zmiany sygnatury metody).
     */
    List<User> getRandomUsers(final int n) {
        final UserMockGenerator userMockGenerator = new UserMockGenerator();

        List<User> userList = userMockGenerator.generate();

        List<User> users = getUserStream().collect(toList());
//
        if (n > users.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return users.stream().limit(n).collect(Collectors.toList());
    }

    /**
     * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się powtarzać, wszystkie zmienną muszą być
     * final. Jeżeli podano liczbę większą niż liczba użytkowników należy wyrzucić wyjątek (bez zmiany sygnatury metody). Napisz to za pomocą strumieni.
     */
    List<User> getRandomUsersAsStream(final int n) {
        final UserMockGenerator userMockGenerator = new UserMockGenerator();

        return Optional.of(userMockGenerator.generate().stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected.stream();
                }))
                .limit(n)
                .distinct()
                .collect(Collectors.toList()))
                .orElseThrow(ArrayIndexOutOfBoundsException::new);
    }


    /**
     * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma pieniędzy
     * na rachunku danego typu przeliczona na złotkówki.
     */
    Map<AccountType, Map<User, BigDecimal>> getAccountUserMoneyInPLNMap() {
        return null;
    }

    /**
     * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma pieniędzy
     * na rachunku danego typu przeliczona na złotkówki.  Napisz to za pomocą strumieni.
     */
    Map<AccountType, Map<User, BigDecimal>> getAccountUserMoneyInPLNMapAsStream() {
        AccountType[] values = AccountType.values();
        List<User> userList = getUserStream().collect(toList());
        for (AccountType value : values) {
            List<User> users = new ArrayList<>();

        }

        return null;
    }

    /**
     * Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana po
     * ilości środków na koncie w kolejności od największej do najmniejszej ich ilości liczonej w złotówkach.
     */

    Map<Permit, List<User>> getUsersByTheyPermitsSorted() {
        return null;
    }

    /**
     * Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana po
     * ilości środków na koncie w kolejności od największej do najmniejszej ich ilości liczonej w złotówkach. Napisz to za pomoca strumieni.
     */

    Map<Permit, List<User>> getUsersByTheyPermitsSortedAsStream() {
        return null;
    }

    /**
     * Podziel użytkowników na tych spełniających podany predykat i na tych niespełniających. Zwracanym typem powinna być mapa Boolean => spełnia/niespełnia,
     * List<Users>
     */
    Map<Boolean, List<User>> divideUsersByPredicate(final Predicate<User> predicate) {

        Map<Boolean, List<User>> users = new HashMap<>();
        List<User> trueUsers = new ArrayList<>();
        List<User> falseUsers = new ArrayList<>();
        for (Holding holding : holdings) {
            for (Company company : holding.getCompanies()) {
                for (User user : company.getUsers()) {
                    if (predicate.test(user)) {
                        trueUsers.add(user);
                    } else {
                        falseUsers.add(user);
                    }
                }
            }
        }
        users.put(true, trueUsers);
        users.put(false, falseUsers);

        return users;
    }

    /**
     * Podziel użytkowników na tych spełniających podany predykat i na tych niespełniających. Zwracanym typem powinna być mapa Boolean => spełnia/niespełnia,
     * List<Users>. Wykonaj zadanie za pomoca strumieni.
     */
    Map<Boolean, List<User>> divideUsersByPredicateAsStream(final Predicate<User> predicate) {
        return getUserStream().collect(partitioningBy(predicate, mapping(user -> user, toList())));
    }

    /**
     * Zwraca strumień wszystkich firm.
     */
    private Stream<Company> getCompanyStream() {
        return holdings.stream().flatMap(holding -> holding.getCompanies().stream());
    }

    /**
     * Zwraca zbiór walut w jakich są rachunki.
     */
    private Set<Currency> getCurenciesSet() {
        return null;
    }

    /**
     * Tworzy strumień rachunków.
     */
    private Stream<Account> getAccountStream() {
        return getUserStream().flatMap(user -> user.getAccounts().stream());
    }

    /**
     * Tworzy strumień użytkowników.
     */
    private Stream<User> getUserStream() {
        return getCompanyStream()
                .flatMap(company -> company.getUsers().stream());
    }

    private ArrayList<String> getAllCurrenciesAsList() {
        return getCompanyStream()
                .flatMap(company -> company.getUsers().stream())
                .flatMap(user -> user.getAccounts().stream())
                .map(account -> account.getCurrency().toString())
                .sorted((o1, o2) -> o1.toString().compareTo(o2.toString()))
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }
}