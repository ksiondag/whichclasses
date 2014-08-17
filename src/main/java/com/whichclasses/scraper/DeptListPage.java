package com.whichclasses.scraper;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.whichclasses.http.HttpUtils;
import com.whichclasses.scraper.DepartmentPage.DepartmentPageFactory;

public class DeptListPage extends CacheableLazyLoadedPage
    implements ContainerPage<DepartmentPage> {

  private static final String DEPARTMENT_LIST_URL =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/DeptList.aspx";
  private final DepartmentPageFactory departmentPageFactory;

  @Inject
  DeptListPage(AuthenticatedClient client, DepartmentPageFactory departmentPageFactory) {
    super(client);
    this.departmentPageFactory = departmentPageFactory;
  }

  @Override
  String getHtmlUrl() {
    return DEPARTMENT_LIST_URL;
  }

  /**
   * @return a list of individual department pages from the department list page
   */
  public Map<String, DepartmentPage> getChildPages() {
    Document document = getDocument();
    Elements departmentLinks = document.select("#GV1 a[href]");
    Map<String, DepartmentPage> departmentPages = Maps.newHashMap();
    for (Element departmentLink : departmentLinks) {
      String departmentId = HttpUtils.getFirstQueryParameter(departmentLink.attr("href"), "crssub");
      if (departmentId != null && departmentId.length() > 0) {
        String departmentName = departmentLink.text();
        // TODO(gunsch): Reduce name in map to department code.
        departmentPages.put(departmentName,
        		departmentPageFactory.create(departmentId, departmentName));
      }
    }

    return departmentPages;
  }
}
