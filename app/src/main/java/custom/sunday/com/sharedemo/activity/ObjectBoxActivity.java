package custom.sunday.com.sharedemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import custom.sunday.com.sharedemo.HomeApplication;
import custom.sunday.com.sharedemo.R;
import custom.sunday.com.sharedemo.base.BaseActivity;
import custom.sunday.com.sharedemo.component.objectbox.data.Student;
import custom.sunday.com.sharedemo.component.objectbox.data.Teacher;

/**
 * Created by zhongfei.sun on 2018/1/26.
 */

public class ObjectBoxActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_box);
        //多对多
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        Student student1 = new Student();
        Student student2 = new Student();
        student1.teachers.add(teacher1);
        student1.teachers.add(teacher2);
        teacher1.students.add(student1);
        teacher1.students.add(student2);
        teacher2.students.add(student2);
        // puts students and teachers
        HomeApplication.getInstance().getBoxStore().boxFor(Student.class).put(student1, student2);
        Teacher teacher = HomeApplication.getInstance().getBoxStore().boxFor(Teacher.class).get(1);
        List<Student> studentList = teacher.students;
        Log.e("sunday","");
    }
}
