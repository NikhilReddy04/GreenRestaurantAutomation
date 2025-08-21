package org.epam.devbistro.pages;

import org.epam.devbistro.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.testng.Assert.assertTrue;


public class DishesPage extends BasePage {



    @FindBy(xpath = "//button[normalize-space()='Appetizers']")
    private WebElement appetizersButton;

    @FindBy(xpath = "//button[normalize-space()='Main Courses']")
    private WebElement mainCoursesButton;

    @FindBy(xpath = "//button[normalize-space()='Desserts']")
    private WebElement dessertsButton;

    @FindBy(xpath = "//p[normalize-space()='Sort by:']/following-sibling::select")
    private WebElement sortingMenu;


    public DishesPage(WebDriver driver) {
        super(driver);
    }

    //actions
    public void clickMenuTypeButton(DishType dishType) {
        switch (dishType) {
            case APPETIZER:
                appetizersButton.click();
                break;

            case MAIN_COURSE:
                mainCoursesButton.click();
                break;

            case DESSERT:
                dessertsButton.click();
                break;
            default:
                throw new IllegalArgumentException("Unknown dish type: " + dishType);
        }
    }

    public void selectSortingOrder(SortingType sortingType) {
        Select selectSort =new Select(sortingMenu);

        switch (sortingType) {
            case MOST_POPULAR:
                selectSort.selectByValue("popularity,desc");
                break;

            case LEAST_POPULAR:
                selectSort.selectByValue("popularity,asc");
                break;

            case LOWER_PRICE:
                selectSort.selectByValue("price,asc");
                break;

            case MORE_PRICE:
                selectSort.selectByValue("price,desc");
                break;

            default:
                throw new IllegalArgumentException("Unknown sorting type: " + sortingType);
        }
    }


}
