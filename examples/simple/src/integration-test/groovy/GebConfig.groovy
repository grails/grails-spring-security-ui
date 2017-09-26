// See: http://www.gebish.org/manual/current/configuration.html
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities

quitCachedDriverOnShutdown = true

environments {

	htmlUnit {
		driver = { new HtmlUnitDriver() }
	}

	phantomjs {
		driver = { new PhantomJSDriver(new DesiredCapabilities()) }
	}

	chrome {
		driver = { new ChromeDriver() }
	}

	firefox {
		driver = { new FirefoxDriver() }
	}
}
