// Updated JotformService.java
package com.LMS.Pulse.service;

import com.LMS.Pulse.model.Jotform;
import com.LMS.Pulse.model.JotformElement;
import com.LMS.Pulse.model.JotformPage;
import com.LMS.Pulse.repository.JotformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JotformService {

    @Autowired
    private JotformRepository jotformRepository;

    public Jotform saveJotform(Map<String, Object> data) {
        Jotform jotform = new Jotform();
        jotform.setJotformName((String) data.get("jotformName"));
        jotform.setTotalPages(getIntegerValue(data.get("totalPages")));

        // Process pages
        List<Map<String, Object>> pagesData = (List<Map<String, Object>>) data.get("pages");
        for (Map<String, Object> pageData : pagesData) {
            JotformPage page = new JotformPage();
            page.setPage(getIntegerValue(pageData.get("page")));
            page.setTotalElements(getIntegerValue(pageData.get("totalElements")));
            page.setJotform(jotform);

            // Process elements
            List<Map<String, Object>> elementsData = (List<Map<String, Object>>) pageData.get("elements");
            for (Map<String, Object> elementData : elementsData) {
                JotformElement element = new JotformElement();
                element.setId(getLongValue(elementData.get("id")));  // Safely convert to Long
                element.setTagName((String) elementData.get("tagName"));
                element.setElementName((String) elementData.get("elementName"));
                element.setContent((String) elementData.get("content"));
                element.setSequence(getIntegerValue(elementData.get("sequence")));
                element.setPage(page);

                page.getElements().add(element);
            }

            jotform.getPages().add(page);
        }

        return jotformRepository.save(jotform);
    }

    // New method to get all unique Jotform names
    public List<String> getAllJotformNames() {
        return jotformRepository.findAll().stream()
                .map(Jotform::getJotformName)
                .distinct()
                .collect(Collectors.toList());
    }

    // Helper method to safely get Integer from Object (handles Long/Integer)
    private Integer getIntegerValue(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        return null;  // Or throw an exception if required
    }

    // Helper method to safely get Long from Object (handles Long/Integer)
    private Long getLongValue(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        return null;  // Or throw an exception if required
    }
}
