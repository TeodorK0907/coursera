package repositories.contracts;

import java.util.List;
import java.util.Map;

public interface StudentCourseRepository extends ReportRepository {

     void getStudentPinList(List<String> studentPins,
                               StringBuilder query,
                               Map<Integer, String> statementMap);


}
