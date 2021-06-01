package unittesting;

import data.CreditRator;
import data.InterestRate;
import dataaccessors.CarDataAccessor;
import entities.Car;
import logic.PaymentCalculator;
import logic.PeriodCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalculationTests {

    @Test
    void getBankInterestRate(){
        assertNotNull(InterestRate.i().todaysRate());
    }

    @Test
    void assumeBankInterestRate(){
        assert(InterestRate.i().todaysRate()>= 3 && InterestRate.i().todaysRate()<=9);
    }
    @Test
    void failBankInterestRate(){
        assertFalse(InterestRate.i().todaysRate()> 9 || InterestRate.i().todaysRate()<3);
    }

    @Test
    void getRkiInterestRating() {
        String expectedRatings[] = {"A","B","C","D"};
        List<String> expectedTitlesList = Arrays.asList(expectedRatings);
        assertTrue(expectedTitlesList.contains((CreditRator.i().rate("1234567898").toString())));
    }

    @Test
    void getRki_should_throw_numberformat_exception_too_many_numbers() {
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            CreditRator.i().rate("122345678901");

        });

        String expectedExceptionMsg = "Illegal CPR number format: \"" + "122345678901" + "\"";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedExceptionMsg));
    }


    @Test
    void downpayment_under_50percent_car_price() {
        Car car = new Car();
        car.setPrice(5000000);
        PaymentCalculator paymentCalculator = new PaymentCalculator();
        paymentCalculator.setCar(car);
        paymentCalculator.setDownPayment(1000);
        assertEquals(1, paymentCalculator.calculateDownPaymentInterestRate());
    }

    @Test
    void downpayment_over_50percent_car_price() {     //how to test???
        Car car = new Car();
        car.setPrice(5000000);
        PaymentCalculator paymentCalculator = new PaymentCalculator();
        paymentCalculator.setCar(car);
        paymentCalculator.setDownPayment(4000000);
        assertEquals(0, paymentCalculator.getDownPaymentInterestRate());
    }

    @Test
    void payment_period_over_three_years_should_return_1() {
        PeriodCalculator periodCalculator = new PeriodCalculator();
        PaymentCalculator paymentCalculator = new PaymentCalculator();
        LocalDate dateOne = LocalDate.of(2021, 05, 05);
        LocalDate dateTwo = LocalDate.of(2025, 05, 05);
        assertEquals(1, paymentCalculator.calculatePaymentPeriodInterestRate(periodCalculator.yearsBetweenDates(dateOne, dateTwo)));
    }

    @Test
    void car_price_after_downpayment_should_return_4999000() {
        Car car = new Car();
        car.setPrice(5000000);
        PaymentCalculator paymentCalculator = new PaymentCalculator();
        paymentCalculator.setCar(car);
        paymentCalculator.setDownPayment(1000);
        assertEquals(4999000, paymentCalculator.calculateCarPriceAfterDownPayment());
    }

    @Test
    void too_high_downpayment_should_throw_exception() {
        Exception exception = assertThrows(ArithmeticException.class, () -> {
            Car car = new Car();
            PaymentCalculator paymentCalculator = new PaymentCalculator();
            paymentCalculator.setCar(car);
            car.setPrice(5000);
            paymentCalculator.setDownPayment(6000);
            paymentCalculator.calculateCarPriceAfterDownPayment();
        });
        String expectedMsg = "Udbetaling overstiger bilens pris.";
        String actualMsg = exception.getMessage();
        assertTrue(actualMsg.contains(expectedMsg));
    }

    @Test
    void downPaymentInterest2(){
        PaymentCalculator paymentCalc = new PaymentCalculator();
        Car car = new Car();
        car.setPrice(5000);
        paymentCalc.setDownPayment(4000);
        paymentCalc.setCar(car);
        assertEquals(0, paymentCalc.calculateDownPaymentInterestRate());
    }

//    @Test
//    void totalInterestRate(){
//        PaymentCalculator paymentCalc = new PaymentCalculator();
//        PeriodCalculator periodCalculator = new PeriodCalculator();
//
//        double rkiAndBankInterestRate = 5;
//        double paymentPeriodInterestRate = paymentCalc.periodInterestRate(periodCalculator.yearsBetweenDates("2021-05-19","2027-05-19"));
//        double downPaymentInterestRate = paymentCalc.downPaymentCalc(10000000.0,2000000.0);
//
//        assertEquals(7,paymentCalc.calculateTotalInterests(rkiAndBankInterestRate,paymentPeriodInterestRate,downPaymentInterestRate));  //snydetest
//    }
//    @Test
//    void totalInterestRate2() {
//        PaymentCalculator paymentCalc = new PaymentCalculator();
//        PeriodCalculator periodCalculator = new PeriodCalculator();
//        LocalDate dateOne = LocalDate.of(2021, 05, 05);
//        LocalDate dateTwo = LocalDate.of(2025, 05 ,05);
//
//        double rkiAndBankInterestRate = 5;
//        double paymentPeriodInterestRate = paymentCalc.calculatePaymentPeriodInterestRate((periodCalculator.yearsBetweenDates(dateOne, dateTwo)));
////        double downPaymentInterestRate = paymentCalc.calculateDownPaymentInterestRate((10000000.0,9000000.0));
//
//        assertEquals(6,paymentCalc.calculateTotalInterests(rkiAndBankInterestRate,paymentPeriodInterestRate,downPaymentInterestRate));
//    }


}

