package pl.netolution.sklep3.service.imports;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface IncomImportCategoriesService {

    @Transactional
    void mergeImportCategories(Map<String, List<String>> categoriesTree, Map<String, String> names);
}
