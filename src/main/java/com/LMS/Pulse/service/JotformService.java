package com.LMS.Pulse.service;

import com.LMS.Pulse.model.Jotform;
import com.LMS.Pulse.model.JotformElement;
import com.LMS.Pulse.model.JotformPage;
import com.LMS.Pulse.repository.JotformRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JotformService {

    @Autowired
    private JotformRepository jotformRepository;

    @Transactional
    public Jotform saveJotform(Map<String, Object> data) {
        Jotform jotform = new Jotform();
        jotform.setJotformName((String) data.get("jotformName"));
        jotform.setTotalPages(getIntegerValue(data.get("totalPages")));

        List<Map<String, Object>> pagesData = (List<Map<String, Object>>) data.get("pages");
        if (pagesData != null) {
            for (Map<String, Object> pageData : pagesData) {
                JotformPage page = new JotformPage();
                page.setPage(getIntegerValue(pageData.get("page")));
                page.setTotalElements(getIntegerValue(pageData.get("totalElements")));
                page.setJotform(jotform);

                List<Map<String, Object>> elementsData = (List<Map<String, Object>>) pageData.get("elements");
                if (elementsData != null) {
                    for (Map<String, Object> elementData : elementsData) {
                        JotformElement element = new JotformElement();
                        element.setId(getLongValue(elementData.get("id")));
                        element.setTagName((String) elementData.get("tagName"));
                        element.setElementName((String) elementData.get("elementName"));
                        element.setContent((String) elementData.get("content"));
                        element.setSequence(getIntegerValue(elementData.get("sequence")));
                        element.setPage(page);
                        page.getElements().add(element);
                    }
                }
                jotform.getPages().add(page);
            }
        }
        return jotformRepository.save(jotform);
    }

    // --- DELETE OPERATION ---
    @Transactional
    public void deleteJotformById(Long id) {
        if (!jotformRepository.existsById(id)) {
            throw new EntityNotFoundException("Jotform with ID " + id + " not found.");
        }
        jotformRepository.deleteById(id);
    }

    public List<Jotform> getAllJotforms() {
        return jotformRepository.findAll();
    }

    // Other existing methods...
    public List<String> getAllJotformNames() {
        // ...
        return jotformRepository.findAll().stream()
                .map(Jotform::getJotformName)
                .distinct()
                .collect(Collectors.toList());
    }

    public Jotform getreact() {
        // ...
        return jotformRepository.findById(1L).orElse(null);
    }

    private Integer getIntegerValue(Object obj) {
        if (obj instanceof Number) return ((Number) obj).intValue();
        return null;
    }

    private Long getLongValue(Object obj) {
        if (obj instanceof Number) return ((Number) obj).longValue();
        return null;
    }
}
