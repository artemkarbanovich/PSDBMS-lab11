package karbanovich.fit.bstu.students.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import karbanovich.fit.bstu.students.Model.Student;

public class StudentDb {

    private static final String STUDENT_TABLE = "STUDENT";


    public static long add(SQLiteDatabase db, Student student) {
        ContentValues values = new ContentValues();

        values.put("IDGROUP", student.getGroupId());
        values.put("NAME", student.getName());
        values.put("BIRTHDAY", student.getBirthday().toString());
        values.put("ADDRESS", student.getAddress());

        return db.insert(STUDENT_TABLE, null, values);
    }

    public static Cursor getStudentsByGroupId(SQLiteDatabase db, int groupId) {
        return db.rawQuery("select * from " + STUDENT_TABLE + " where IDGROUP = " + groupId + ";", null);
    }
}
