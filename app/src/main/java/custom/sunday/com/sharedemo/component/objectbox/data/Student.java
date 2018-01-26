package custom.sunday.com.sharedemo.component.objectbox.data;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * Created by zhongfei.sun on 2018/1/26.
 */
@Entity
public class Student {
    @Id
    public long id;
    public ToMany<Teacher> teachers;
}
