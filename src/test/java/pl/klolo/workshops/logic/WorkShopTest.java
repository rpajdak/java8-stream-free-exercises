package pl.klolo.workshops.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import pl.klolo.workshops.domain.Account;
import pl.klolo.workshops.domain.AccountType;
import pl.klolo.workshops.domain.Currency;
import pl.klolo.workshops.domain.Permit;
import pl.klolo.workshops.domain.User;

@RunWith(BlockJUnit4ClassRunner.class)
public class WorkShopTest {

  private WorkShop workShop;

  @Before
  public void setUp() {
    workShop = new WorkShop();
  }

  /**
   * 1.
   */
  @Test
  public void shouldReturnAmountOfHoldingWhereIsAtLeastOneCompany() {
    final long amountOfCompanies = workShop.getHoldingsWhereAreCompanies();
    assertEquals(3, amountOfCompanies);
  }

  /**
   * 2.
   */
  @Test
  public void shouldReturnLowerCaseNameOfAllHoldings() {
    final List<String> holdingNames = workShop.getHoldingNames();
    assertEquals("[nestle, coca-cola, pepsico]", holdingNames.toString());
  }

  /**
   * 3.
   */
  @Test
  public void shouldReturnNamesOfAllHoldingInString() {
    final String holdingNames = workShop.getHoldingNamesAsString();
    assertEquals("(Coca-Cola, Nestle, Pepsico)", holdingNames);
  }

  /**
   * 4.
   */
  @Test
  public void shouldCountCompaniesInHoldings() {
    final long companiesAmount = workShop.getCompaniesAmount();
    assertEquals(8, companiesAmount);
  }

  /**
   * 5.
   */
  @Test
  public void shouldCountAllUsersInAllCompanies() {
    final long userAmount = workShop.getAllUserAmount();
    assertEquals(20, userAmount);
  }

  /**
   * 6.
   */
  @Test
  public void shouldReturnAllCompaniesName() {
    final List<String> allCompaniesName = workShop.getAllCompaniesNames();
    assertEquals("[Nescafe, Gerber, Nestea, Fanta, Sprite, Lays, Pepsi, Mirinda]", allCompaniesName.toString());
  }

  /**
   * 7.
   */
  @Test
  public void shouldReturnAllCompaniesNameAsString() {
    final String allCompaniesName = workShop.getAllCompaniesNamesAsString();
    assertEquals("Nescafe+Gerber+Nestea+Fanta+Sprite+Lays+Pepsi+Mirinda", allCompaniesName);
  }

  /**
   * 8.
   */
  @Test
  public void shouldReturnAllCompaniesNameAsStringUsingStringBuilder() {
    final String allCompaniesName = workShop.getAllCompaniesNamesAsStringUsingStringBuilder();
    assertEquals("Nescafe+Gerber+Nestea+Fanta+Sprite+Lays+Pepsi+Mirinda", allCompaniesName);
  }

  /**
   * 9.
   */
  @Test
  public void shouldReturnHowMuchAccountHaveUsers() {
    final long accountAmount = workShop.getAllUserAccountsAmount();
    assertEquals(35, accountAmount);
  }

  /**
   * 10.
   */
  @Test
  public void shouldReturnAllCompaniesNameAsLinkedList() {
    final LinkedList<String> allCompaniesName = workShop.getAllCompaniesNamesAsLinkedList();
    assertEquals("[Nescafe, Gerber, Nestea, Fanta, Sprite, Lays, Pepsi, Mirinda]", allCompaniesName.toString());
  }

  /**
   * 11.
   */
  @Test
  public void shouldReturnSetOfAllCurrencies() {
    final String allUsedCurrecies = workShop.getAllCurrencies();
    assertEquals("CHF, EUR, PLN, USD", allUsedCurrecies);
  }

  /**
   * 13.
   */
  @Test
  public void shouldReturnHowManyWomenAreInCompanies() {
    final long womanAmount = workShop.getWomanAmount();
    assertEquals(4, womanAmount);
  }

  /**
   * 14.
   */
  @Test
  public void shouldCalculateAmountInPln() {
    final Account accountWithOneZloty = Account.builder()
        .amount(new BigDecimal("1.0"))
        .currency(Currency.PLN)
        .build();

    assertEquals(new BigDecimal("1.000"), workShop.getAccountAmountInPLN(accountWithOneZloty));

    final Account accountWithOneDolar = Account.builder()
        .amount(new BigDecimal("1.0"))
        .currency(Currency.USD)
        .build();
    ;
    assertEquals(new BigDecimal("3.720"), workShop.getAccountAmountInPLN(accountWithOneDolar).setScale(3, BigDecimal.ROUND_HALF_DOWN));
  }

  /**
   * 15.
   */
  @Test
  public void shouldGetTotalCashInPLNCorrectlySum() {
    final List<Account> accounts = Arrays.asList(
        Account.builder().amount(new BigDecimal(150)).currency(Currency.PLN).build(), // 150 PLN
        Account.builder().amount(new BigDecimal(50)).currency(Currency.USD).build(), // 186 PLN
        Account.builder().amount(new BigDecimal(300)).currency(Currency.PLN).build() // 300 PLN
    );

    assertEquals(636, workShop.getTotalCashInPLN(accounts).intValue());
  }

  /**
   * 16.
   */
  @Test
  public void shouldReturnUserNameForPassedCondition() {
    assertEquals("[Adam, Alfred, Amadeusz]", workShop.getUsersForPredicate(user -> user.getFirstName().startsWith("A")).toString());
    assertEquals("[Karol, Zosia]", workShop.getUsersForPredicate(user -> user.getAge() > 50).toString());
  }

  /**
   * 17.
   */
  @Test
  public void shouldReturnWomanWhichAreOlderThan50() {
    final List<String> oldWoman = workShop.getOldWoman(50);
    assertEquals("[Zosia]", oldWoman.toString());
  }

  /**
   * 18.
   */
  @Test
  public void shouldExecuteConsumerForEachCompany() {
    final StringBuilder builder = new StringBuilder();
    workShop.executeForEachCompany(company ->
        builder
            .append(company.getName())
            .append("=")
            .append(company.getUsers().size())
            .append(" ")
    );

    assertEquals("Nescafe=4 Gerber=3 Nestea=1 Fanta=3 Sprite=2 Lays=2 Pepsi=3 Mirinda=2 ", builder.toString());
  }

  /**
   * 19.
   */
  @Test
  public void shouldGetRichestWoman() {
    final Optional<User> richestWoman = workShop.getRichestWoman();
    assertEquals("Zosia Psikuta", richestWoman.get().getFirstName() + " " + richestWoman.get().getLastName());
  }

  /**
   * 20.
   */
  @Test
  public void shouldReturnNamesOfFirstNCompany() {
    final Set<String> first10Company = workShop.getFirstNCompany(5);
    assertEquals("[Sprite, Gerber, Fanta, Nescafe, Nestea]", first10Company.toString());
  }

  /**
   * 21.
   */
  @Test
  public void shouldFindROR1AsMostUsedAccountType() {
    final AccountType mostUseAccountType = workShop.getMostPopularAccountType();
    assertEquals(AccountType.ROR1, mostUseAccountType);
  }

  /**
   * 21.
   */
  @Test
  public void shouldGetUserFoPassedPredicate() {
    final User user = workShop.getUser(u -> u.getFirstName().equals("Adam"));
    assertEquals("Wojcik", user.getLastName());
  }

  /**
   * 22.
   */
  @Test(expected = IllegalArgumentException.class)
  public void shouldGetUserFoPassedPredicateThrowException() {
    workShop.getUser(u -> u.getFirstName().equals("Camillo"));
  }

  /**
   * 23.
   */
  @Test
  public void shouldReturnCompanyMapWithUserList() {
    final Map<String, List<User>> companies = workShop.getUserPerCompany();
    assertEquals(8, companies.size());
    assertEquals("Bazuka", companies.get("Sprite").get(0).getLastName());
  }

  /**
   * 24.
   */
  @Test
  public void shouldReturnCompanyMapWithUserListAsString() {
    final Map<String, List<String>> companies = workShop.getUserPerCompanyAsString();
    assertEquals(8, companies.size());
    assertEquals("Jan Bazuka", companies.get("Sprite").get(0));
  }

  /**
   * 25.
   */
  @Test
  public void shouldReturnCompanyMapWithUserListUsingPassedFunction() {
    final Function<User, String> convertUserToString = user -> user.getFirstName() + " " + user.getLastName() + ": " + user.getAccounts().size();
    final Map<String, List<String>> companies = workShop.getUserPerCompany(convertUserToString);

    assertEquals(8, companies.size());
    assertEquals("Jan Bazuka: 3", companies.get("Sprite").get(0));
  }

  /**
   * 26.
   */
  @Test
  public void shouldSegregateUserBySex() {
    final Map<Boolean, Set<String>> usersBySex = workShop.getUserBySex();
    assertEquals(13, usersBySex.get(true).size());
    assertEquals(4, usersBySex.get(false).size());

    assertTrue(usersBySex.get(true).contains("Mocarz"));
    assertTrue(usersBySex.get(false).contains("Warszawska"));
  }

  /**
   * 27.
   */
  @Test
  public void shouldCreateAccountsMap() {
    final Map<String, Account> accounts = workShop.createAccountsMap();
    assertTrue(accounts.size() == 35);
  }

  /**
   * 28.
   */
  @Test
  public void shouldCreateListOfUserNames() {
    final String userNames = workShop.getUserNames();

    assertNotNull(userNames);
    assertTrue(userNames.startsWith("Adam Alfred Amadeusz Bartek Filip"));
  }

  /**
   * 29.
   */
  @Test
  public void shouldCreateUserSet() {
    final Set<User> users = workShop.getUsers();

    assertEquals(10, users.size());
  }

  /**
   * 31.
   */
  @Test
  public void shouldFindUser() {
    final Optional<User> user = workShop.findUser(u -> u.getLastName().equals("Psikuta"));

    assertTrue(user.isPresent());
    assertEquals("Zosia", user.get().getFirstName());
  }

  /**
   * 32.
   */
  @Test
  public void shouldGetUserAdultantStatus() {
    final Optional<User> user = workShop.findUser(u -> u.getLastName().equals("Psikuta"));
    final String adultatStatusOfPsikuta = workShop.getAdultantStatusAsStream(user);

    assertNotNull(adultatStatusOfPsikuta);
    assertEquals("Zosia Psikuta ma lat 67", adultatStatusOfPsikuta);

    final Optional<User> userNotExisted = workShop.findUser(u -> u.getLastName().equals("Komorwski"));
    final String adultantStatusNotExisted = workShop.getAdultantStatusAsStream(userNotExisted);

    assertNotNull(adultantStatusNotExisted);
    assertEquals("Brak użytkownika", adultantStatusNotExisted);
  }

  /**
   * 33.
   */
  @Test
  public void shouldSortAndDisplayUser() {
    workShop.showAllUser();
  }

  /**
   * 34.
   */
  @Test
  public void shouldCountMoneyOnAllAccounts() {
    final Map<AccountType, BigDecimal> moneyOnAccount = workShop.getMoneyOnAccounts();

    assertEquals(new BigDecimal("87461"), moneyOnAccount.get(AccountType.LO2).setScale(0, BigDecimal.ROUND_HALF_DOWN));
  }

  /**
   * 35.
   */
  @Test
  public void shouldCalculateSumOfSquareAge() {
    final int sumOfSquareAges = workShop.getAgeSquaresSum();
    assertEquals(27720, sumOfSquareAges);
  }

  /**
   * 36.
   */
  @Test
  public void shouldGetRandomNUser() {
    final List<User> randomUsers = workShop.getRandomUsers(4);

    assertEquals(4, new HashSet<>(randomUsers).size());
  }

  /**
   * 37.
   */
  @Test(timeout = 15) // maksymalnie 25ms jezeli masz wolny komputer.
  public void shouldGetFastRandomNUser() {
    final List<User> randomUsers = workShop.getRandomUsers(20);

    assertEquals(20, randomUsers.size());
  }

  /**
   * 1.
   */
  @Test
  public void shouldReturnAmountOfHoldingWhereIsAtLeastOneCompanyAsStream() {
    final long amountOfCompanies = workShop.getHoldingsWhereAreCompaniesAsStream();
    assertEquals(3, amountOfCompanies);
  }

  /**
   * 2.
   */
  @Test
  public void shouldReturnLowerCaseNameOfAllHoldingsAsStream() {
    final List<String> holdingNames = workShop.getHoldingNamesAsStream();
    assertEquals("[nestle, coca-cola, pepsico]", holdingNames.toString());
  }

  /**
   * 3.
   */
  @Test
  public void shouldReturnNamesOfAllHoldingInStringAsStream() {
    final String holdingNames = workShop.getHoldingNamesAsStringAsStream();
    assertEquals("(Coca-Cola, Nestle, Pepsico)", holdingNames);
  }

  /**
   * 4.
   */
  @Test
  public void shouldCountCompaniesInHoldingsAsStream() {
    final long companiesAmount = workShop.getCompaniesAmountAsStream();
    assertEquals(8, companiesAmount);
  }

  /**
   * 5.
   */
  @Test
  public void shouldCountAllUsersInAllCompaniesAsStream() {
    final long userAmount = workShop.getAllUserAmountAsStream();
    assertEquals(20, userAmount);
  }

  /**
   * 6.
   */
  @Test
  public void shouldReturnAllCompaniesNameAsStream() {
    final List<String> allCompaniesName = workShop.getAllCompaniesNamesAsStream();
    assertEquals("[Nescafe, Gerber, Nestea, Fanta, Sprite, Lays, Pepsi, Mirinda]", allCompaniesName.toString());
  }

  /**
   * 7.
   */
  @Test
  public void shouldReturnAllCompaniesNameAsStringAsStream() {
    final String allCompaniesName = workShop.getAllCompaniesNamesAsStringAsStream();
    assertEquals("Nescafe+Gerber+Nestea+Fanta+Sprite+Lays+Pepsi+Mirinda", allCompaniesName);
  }

  /**
   * 9.
   */
  @Test
  public void shouldReturnHowMuchAccountHaveUsersAsStream() {
    final long accountAmount = workShop.getAllUserAccountsAmountAsStream();
    assertEquals(35, accountAmount);
  }

  /**
   * 10.
   */
  @Test
  public void shouldReturnAllCompaniesNameAsLinkedListAsStream() {
    final LinkedList<String> allCompaniesName = workShop.getAllCompaniesNamesAsLinkedListAsStream();
    assertEquals("[Nescafe, Gerber, Nestea, Fanta, Sprite, Lays, Pepsi, Mirinda]", allCompaniesName.toString());
  }

  /**
   * 11.
   */
  @Test
  public void shouldReturnSetOfAllCurrenciesAsStream() {
    final String allUsedCurrecies = workShop.getAllCurrenciesAsStream();
    assertEquals("CHF, EUR, PLN, USD", allUsedCurrecies);
  }

  /**
   * 13.
   */
  @Test
  public void shouldReturnHowManyWomenAreInCompaniesAsStream() {
    final long womanAmount = workShop.getWomanAmountAsStream();
    assertEquals(4, womanAmount);
  }

  /**
   * 14.
   */
  @Test
  public void shouldCalculateAmountInPlnAsStream() {
    final Account accountWithOneZloty = Account.builder()
        .amount(new BigDecimal("1.0"))
        .currency(Currency.PLN)
        .build();

    assertEquals(new BigDecimal("1.000"), workShop.getAccountAmountInPLNAsStream(accountWithOneZloty));

    final Account accountWithOneDolar = Account.builder()
        .amount(new BigDecimal("1.0"))
        .currency(Currency.USD)
        .build();
    ;
    assertEquals(new BigDecimal("3.720"), workShop.getAccountAmountInPLNAsStream(accountWithOneDolar).setScale(3, BigDecimal.ROUND_HALF_DOWN));
  }

  /**
   * 15.
   */
  @Test
  public void shouldGetTotalCashInPLNCorrectlySumAsStream() {
    final List<Account> accounts = Arrays.asList(
        Account.builder().amount(new BigDecimal(150)).currency(Currency.PLN).build(), // 150 PLN
        Account.builder().amount(new BigDecimal(50)).currency(Currency.USD).build(), // 186 PLN
        Account.builder().amount(new BigDecimal(300)).currency(Currency.PLN).build() // 300 PLN
    );

    assertEquals(636, workShop.getTotalCashInPLNAsStream(accounts).intValue());
  }

  /**
   * 16.
   */
  @Test
  public void shouldReturnUserNameForPassedConditionAsStream() {
    assertEquals("[Adam, Alfred, Amadeusz]", workShop.getUsersForPredicateAsStream(user -> user.getFirstName().startsWith("A")).toString());
    assertEquals("[Karol, Zosia]", workShop.getUsersForPredicateAsStream(user -> user.getAge() > 50).toString());
  }

  /**
   * 17.
   */
  @Test
  public void shouldReturnWomanWhichAreOlderThan50AsStream() {
    final List<String> oldWomam = workShop.getOldWomanAsStream(50);
    assertEquals("[Zosia]", oldWomam.toString());
  }

  /**
   * 19.
   */
  @Test
  public void shouldGetRichestWomanAsStream() {
    final Optional<User> richestWoman = workShop.getRichestWomanAsStream();
    assertEquals("Zosia Psikuta", richestWoman.get().getFirstName() + " " + richestWoman.get().getLastName());
  }

  /**
   * 20.
   */
  @Test
  public void shouldReturnNamesOfFirstNCompanyAsStream() {
    final Set<String> first10Company = workShop.getFirstNCompanyAsStream(5);
    assertEquals("[Sprite, Gerber, Fanta, Nescafe, Nestea]", first10Company.toString());
  }

  /**
   * 21.
   */
  @Test
  public void shouldFindROR1AsMostUsedAccountTypeAsStream() {
    final AccountType mostUseAccoutType = workShop.getMostPopularAccountTypeAsStream();
    assertEquals(AccountType.ROR1, mostUseAccoutType);
  }

  /**
   * 21.
   */
  @Test
  public void shouldGetUserFoPassedPredicateAsStream() {
    final User user = workShop.getUserAsStream(u -> u.getFirstName().equals("Adam"));
    assertEquals("Wojcik", user.getLastName());
  }

  /**
   * 22.
   */
  @Test(expected = IllegalArgumentException.class)
  public void shouldGetUserFoPassedPredicateThrowExceptionAsStream() {
    workShop.getUserAsStream(u -> u.getFirstName().equals("Camillo"));
  }

  /**
   * 23.
   */
  @Test
  public void shouldReturnCompanyMapWithUserListAsStream() {
    final Map<String, List<User>> companies = workShop.getUserPerCompanyAsStream();
    assertEquals(8, companies.size());
    assertEquals("Bazuka", companies.get("Sprite").get(0).getLastName());
  }

  /**
   * 24.
   */
  @Test
  public void shouldReturnCompanyMapWithUserListAsStringAsStream() {
    final Map<String, List<String>> companies = workShop.getUserPerCompanyAsStringAsStream();
    assertEquals(8, companies.size());
    assertEquals("Jan Bazuka", companies.get("Sprite").get(0));
  }

  /**
   * 25.
   */
  @Test
  public void shouldReturnCompanyMapWithUserListUsingPassedFunctionAsStream() {
    final Function<User, String> convertUserToString = user -> user.getFirstName() + " " + user.getLastName() + ": " + user.getAccounts().size();
    final Map<String, List<String>> companies = workShop.getUserPerCompanyAsStream(convertUserToString);

    assertEquals(8, companies.size());
    assertEquals("Jan Bazuka: 3", companies.get("Sprite").get(0));
  }

  /**
   * 26.
   */
  @Test
  public void shouldSegregateUserBySexAsStream() {
    final Map<Boolean, Set<String>> usersBySex = workShop.getUserBySexAsStream();
    assertEquals(13, usersBySex.get(true).size());
    assertEquals(4, usersBySex.get(false).size());

    assertTrue(usersBySex.get(true).contains("Mocarz"));
    assertTrue(usersBySex.get(false).contains("Warszawska"));
  }

  /**
   * 27.
   */
  @Test
  public void shouldCreateAccountsMapAsStream() {
    final Map<String, Account> accounts = workShop.createAccountsMapAsStream();
    assertTrue(accounts.size() == 35);
  }

  /**
   * 28.
   */
  @Test
  public void shouldCreateListOfUserNamesAsStream() {
    final String userNames = workShop.getUserNamesAsStream();

    assertNotNull(userNames);
    assertTrue(userNames.startsWith("Adam Alfred Amadeusz Bartek Filip"));
  }

  /**
   * 29.
   */
  @Test
  public void shouldCreateUserSetAsStream() {
    final Set<User> users = workShop.getUsersAsStream();

    assertEquals(10, users.size());
  }

  /**
   * 31.
   */
  @Test
  public void shouldFindUserAsStream() {
    final Optional<User> user = workShop.findUserAsStream(u -> u.getLastName().equals("Psikuta"));

    assertTrue(user.isPresent());
    assertEquals("Zosia", user.get().getFirstName());
  }

  /**
   * 32.
   */
  @Test
  public void shouldGetUserAdultantStatusAsStream() {
    final Optional<User> user = workShop.findUserAsStream(u -> u.getLastName().equals("Psikuta"));
    final String adultatStatusOfPsikuta = workShop.getAdultantStatusAsStream(user);

    assertNotNull(adultatStatusOfPsikuta);
    assertEquals("Zosia Psikuta ma lat 67", adultatStatusOfPsikuta);

    final Optional<User> userNotExisted = workShop.findUser(u -> u.getLastName().equals("Komorwski"));
    final String adultantStatusNotExisted = workShop.getAdultantStatusAsStream(userNotExisted);

    assertNotNull(adultantStatusNotExisted);
    assertEquals("Brak użytkownika", adultantStatusNotExisted);
  }

  /**
   * 33.
   */
  @Test
  public void shouldSortAndDisplayUserAsStream() {
    workShop.showAllUserAsStream();
  }

  /**
   * 34.
   */
  @Test
  public void shouldCountMoneyOnAllAccountsAsStream() {
    final Map<AccountType, BigDecimal> moneyOnAccount = workShop.getMoneyOnAccountsAsStream();

    assertEquals(new BigDecimal("87461"), moneyOnAccount.get(AccountType.LO2).setScale(0, BigDecimal.ROUND_HALF_DOWN));
  }

  /**
   * 35.
   */
  @Test
  public void shouldCalculateSumOfSquareAgeAsStream() {
    final int sumOfSquareAges = workShop.getAgeSquaresSumAsStream();
    assertEquals(27720, sumOfSquareAges);
  }

  /**
   * 36.
   */
  @Test
  public void shouldGetRandomNUserAsStream() {
    final List<User> randomUsers = workShop.getRandomUsersAsStream(4);

    assertEquals(4, new HashSet<>(randomUsers).size());
  }

  /**
   * 37.
   */
  @Test(timeout = 15) // maksymalnie 25ms jezeli masz wolny komputer.
  public void shouldGetFastRandomNUserAsStream() {
    final List<User> randomUsers = workShop.getRandomUsersAsStream(20);

    assertEquals(20, randomUsers.size());
  }

  /**
   * 38. Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma
   * pieniędzy na rachunku danego typu przeliczona na złotkówki.
   */
  @Test
  public void shouldGetAccountUserMoneyInPLNMap() {
    Map<AccountType, Map<User, BigDecimal>> accountUserMoneyInPLNMap = workShop.getAccountUserMoneyInPLNMap();
    assertNotNull(accountUserMoneyInPLNMap);
    assertEquals(accountUserMoneyInPLNMap.size(), 6L);
    assertEquals(accountUserMoneyInPLNMap.get(AccountType.LO1).size(), 6L);
    assertEquals(accountUserMoneyInPLNMap.get(AccountType.ROR1).size(), 3L);
    assertEquals(accountUserMoneyInPLNMap.get(AccountType.ROR1).values().stream().reduce(BigDecimal::add).get(), BigDecimal.valueOf(13151.04));
    assertEquals(accountUserMoneyInPLNMap.get(AccountType.LO1).values().stream().reduce(BigDecimal::add).get(), BigDecimal.valueOf(105864.81));
  }

  /**
   * 38. Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma
   * pieniędzy na rachunku danego typu przeliczona na złotkówki.
   */
  @Test
  public void shouldGetAccountUserMoneyInPLNMapAsStream() {
    Map<AccountType, Map<User, BigDecimal>> accountUserMoneyInPLNMap = workShop.getAccountUserMoneyInPLNMapAsStream();
    assertNotNull(accountUserMoneyInPLNMap);
    assertEquals(accountUserMoneyInPLNMap.size(), 6);
    assertEquals(accountUserMoneyInPLNMap.get(AccountType.LO1).size(), 6);
    assertEquals(accountUserMoneyInPLNMap.get(AccountType.ROR1).size(), 3);
    assertEquals(accountUserMoneyInPLNMap.get(AccountType.ROR1).values().stream().reduce(BigDecimal::add).get(), BigDecimal.valueOf(13151.04));
    assertEquals(accountUserMoneyInPLNMap.get(AccountType.LO1).values().stream().reduce(BigDecimal::add).get(), BigDecimal.valueOf(105864.81));
  }

  /**
   * 40. Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana
   * po ilości środków na koncie w kolejności od największej do najmniejszej ich ilości.
   */
  @Test
  public void shouldReturnUsersByTheyPermitsSorted() {
    Map<Permit, List<User>> usersByTheyPermitsSorted = workShop.getUsersByTheyPermitsSorted();
    assertNotNull(usersByTheyPermitsSorted);
    assertEquals(usersByTheyPermitsSorted.size(), 4);
    assertEquals(usersByTheyPermitsSorted.get(Permit.LOAN).size(), 16);
    assertEquals(usersByTheyPermitsSorted.get(Permit.LOAN).get(0).getLastName(), "Bazuka");
    assertEquals(usersByTheyPermitsSorted.get(Permit.LOAN).get(15).getLastName(), "Dreh");
    assertEquals(usersByTheyPermitsSorted.get(Permit.ORDER_HISTORY).size(), 13);
    assertEquals(usersByTheyPermitsSorted.get(Permit.ORDER_HISTORY).get(0).getLastName(), "Marcinowicz");
    assertEquals(usersByTheyPermitsSorted.get(Permit.ORDER_HISTORY).get(12).getLastName(), "Dreh");
  }

  /**
   * 40. Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana
   * po ilości środków na koncie w kolejności od największej do najmniejszej ich ilości.
   */
  @Test
  public void shouldReturnUsersByTheyPermitsSortedAsStream() {
    Map<Permit, List<User>> usersByTheyPermitsSorted = workShop.getUsersByTheyPermitsSortedAsStream();
    assertNotNull(usersByTheyPermitsSorted);
    assertEquals(usersByTheyPermitsSorted.size(), 4);
    assertEquals(usersByTheyPermitsSorted.get(Permit.LOAN).size(), 16);
    assertEquals(usersByTheyPermitsSorted.get(Permit.LOAN).get(0).getLastName(), "Bazuka");
    assertEquals(usersByTheyPermitsSorted.get(Permit.LOAN).get(15).getLastName(), "Dreh");
    assertEquals(usersByTheyPermitsSorted.get(Permit.ORDER_HISTORY).size(), 13);
    assertEquals(usersByTheyPermitsSorted.get(Permit.ORDER_HISTORY).get(0).getLastName(), "Marcinowicz");
    assertEquals(usersByTheyPermitsSorted.get(Permit.ORDER_HISTORY).get(12).getLastName(), "Dreh");
  }

  @Test
  public void shouldDivideUsersByPredicate() {
    final Map<Boolean, List<User>> usersByPredicate = workShop.divideUsersByPredicate(user -> user.getPermits().size() > 1);
    assertNotNull(usersByPredicate);
    assertEquals(16, usersByPredicate.get(true).size());
    assertEquals(4, usersByPredicate.get(false).size());
    final Map<Boolean, List<User>> usersByPredicate2 = workShop.divideUsersByPredicate(user -> user.getAge() > 30);
    assertNotNull(usersByPredicate2);
    assertEquals(11, usersByPredicate2.get(true).size());
    assertEquals(9, usersByPredicate2.get(false).size());
  }

  @Test
  public void shouldDivideUsersByPredicateAsStream() {
    final Map<Boolean, List<User>> usersByPredicate = workShop.divideUsersByPredicateAsStream(user -> user.getPermits().size() > 1);
    assertNotNull(usersByPredicate);
    assertEquals(16, usersByPredicate.get(true).size());
    assertEquals(4, usersByPredicate.get(false).size());
    final Map<Boolean, List<User>> usersByPredicate2 = workShop.divideUsersByPredicateAsStream(user -> user.getAge() > 30);
    assertNotNull(usersByPredicate2);
    assertEquals(11, usersByPredicate2.get(true).size());
    assertEquals(9, usersByPredicate2.get(false).size());
  }
}
