package com.whichclasses;

import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.whichclasses.http.Frontend;
import com.whichclasses.http.WhichClassesServerModule;
import com.whichclasses.scraper.Scraper;
import com.whichclasses.scraper.ScraperModule;

public class WhichClasses {

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(
        new ScraperModule(),
        new WhichClassesServerModule());

    Logger.getGlobal().info("Begin scrape");
    injector.getInstance(Scraper.class).runScrape();
    Logger.getGlobal().info("Scrape finished");

    // startServing() must be called last, since it will tie up the
    // main thread until exiting or encountering an issue.
    Logger.getGlobal().info("Start serving");
    injector.getInstance(Frontend.class).startServing();
  }

}