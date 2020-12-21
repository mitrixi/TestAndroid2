package service;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static service.TestUtils.getSwipeDirection;

public class ios {
    public static void scrollToElementByXpaths(IOSDriver<WebElement> driver, List<String> xpaths) {
        // ToDo Тесты показали, что во время swipe элементы DOM-дерева находящиеся выше могут пропадать из DOM, соотв mobileElement НЕ будет находится...
        //      Почему это происходит и как решать проблему - непонятно. Так же непонятно как определить: текущее положение scroll; убедиться что мы уперлись в верхнюю границу
        //      Вероятно универсального механизма 'swipe до нужного элемента' не существует (ранее существовавщая реализация AppiumDriver.swipe() была убрана)

        // На 1 экран вверх (возможно это лишнее)
//        HashMap paramsUpScroll = new HashMap<>();
//        paramsUpScroll.put("direction", "down");
//        driver.executeScript("mobile: swipe", paramsUpScroll);

        // Находим элемент и проверяем на экране ли (isDisplayed), если да, то возвращаемся сразу
        MobileElement mobileElement = null;
        for (String xpath: xpaths) {
            mobileElement = mobileElement == null ? (MobileElement) driver.findElementByXPath(xpath) : mobileElement.findElementByXPath(xpath);
        }
        if (mobileElement == null || !mobileElement.isEnabled()) {
            System.out.println("Ошибка сценария: scrollToElementByXpaths нет элемента на странице");
            return;
        }
        if (mobileElement != null && mobileElement.isDisplayed())
            return;

        // Размеры экрана телефона
        int displayWidth = driver.manage().window().getSize().width;
        int displayHeight = driver.manage().window().getSize().height;

        // Определяем выше или ниже искомый mobileElement относительно центра экрана
        String swipeDirection = getSwipeDirection(displayWidth, mobileElement.getLocation().y);

        boolean isDisplayed = false;

        while (!isDisplayed) {
            HashMap<String, Object> swipe = new HashMap<>();
            swipe.put("direction", swipeDirection);
            driver.executeScript("mobile: swipe", swipe);

            isDisplayed = mobileElement.isDisplayed();
            if (!isDisplayed) {
                // Если после текущего свайпа элемент "проскочил"
                if (!swipeDirection.equals(getSwipeDirection(displayWidth, mobileElement.getLocation().y))) {
                    // Корректируем "немного" положение экрана в обратном направлении (относительно swipeDirection)
                    Map<String, Object> dragFromToForDuration = new HashMap<>();
                    dragFromToForDuration.put("duration", 0);
                    dragFromToForDuration.put("fromX", displayWidth / 2);
                    dragFromToForDuration.put("toX", displayWidth / 2);
                    if (swipeDirection.equals("down")) {
                        dragFromToForDuration.put("fromY", displayHeight / 2 + displayHeight / 12);
                        dragFromToForDuration.put("toY", displayHeight / 2);
                    }
                    else {
                        dragFromToForDuration.put("fromY", displayHeight / 2);
                        dragFromToForDuration.put("toY", displayHeight / 2 + displayHeight / 12);
                    }
                    driver.executeScript("mobile: dragFromToForDuration", dragFromToForDuration);

                    // Выходим принудительно, т.к. считаем что элемент уже isDisplayed
                    isDisplayed = true;
                    break;
                }
            }
        }
    }

}
