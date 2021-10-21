package karbanovich.fit.bstu.students.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;

import karbanovich.fit.bstu.students.Model.Progress;

public class ProgressDb {

    private static final String FACULTY_TABLE = "FACULTY";
    private static final String GROUP_TABLE = "[GROUP]";
    private static final String STUDENT_TABLE = "STUDENT";
    private static final String SUBJECT_TABLE = "SUBJECT";
    private static final String PROGRESS_TABLE = "PROGRESS";


    public static long add(SQLiteDatabase db, Progress progress) {
        ContentValues values = new ContentValues();

        values.put("IDSTUDENT", progress.getStudentId());
        values.put("IDSUBJECT", progress.getSubjectId());
        values.put("EXAMDATE", progress.getDate().toString());
        values.put("MARK", progress.getMark());
        values.put("TEACHER", progress.getTeacher());

        return db.insert(PROGRESS_TABLE, null, values);
    }

    public static Cursor getBestStudByFaculty(SQLiteDatabase db, int facultyId, String startDate, String endDate) {

        String query = "select " + STUDENT_TABLE + ".NAME, avg(" + PROGRESS_TABLE + ".MARK)"
                        + " from " + PROGRESS_TABLE
                            + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                            + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                        + " where " + GROUP_TABLE + ".IDFACULTY = " + facultyId
                            + " and EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                            + " and not exists (select MARK from " + PROGRESS_TABLE
                                + " where MARK < 4"
                                    + " and " + PROGRESS_TABLE + ".IDSTUDENT = " + STUDENT_TABLE + ".IDSTUDENT"
                                    + " and EXAMDATE between date('" + startDate + "') and date('" + endDate + "'))"
                        + " group by " + PROGRESS_TABLE + ".IDSTUDENT"
                        + " order by avg(" + PROGRESS_TABLE + ".MARK) desc"
                        + " limit 5";

        return db.rawQuery(query, null);
    }

    public static Cursor getUnderperfStudByFaculty(SQLiteDatabase db, int facultyId, String startDate, String endDate) {

        String query = "select "
                            + STUDENT_TABLE + ".NAME,"
                            + " (select count(MARK)"
                                + " from " + PROGRESS_TABLE
                                + " where MARK < 4"
                                    + " and " + PROGRESS_TABLE + ".IDSTUDENT = " + STUDENT_TABLE + ".IDSTUDENT"
                                    + " and EXAMDATE between date('" + startDate + "') and date('" + endDate + "')) as countMarks"
                        + " from " + PROGRESS_TABLE
                            + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                            + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                        + " where " + GROUP_TABLE + ".IDFACULTY = " + facultyId
                            + " and EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                            + " and countMarks >= 1"
                        + " group by " + PROGRESS_TABLE + ".IDSTUDENT"
                        + " order by countMarks desc";

        return db.rawQuery(query, null);
    }

    public static Cursor getComparByFaculty(SQLiteDatabase db, String startDate, String endDate) {

        String query = "select " + FACULTY_TABLE + ".FACULTY, avg(" + PROGRESS_TABLE + ".MARK)"
                        + " from " + PROGRESS_TABLE
                            + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                            + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                            + " join " + FACULTY_TABLE + " on " + FACULTY_TABLE + ".IDFACULTY = " + GROUP_TABLE + ".IDFACULTY"
                        + " group by " + FACULTY_TABLE + ".IDFACULTY"
                        + " order by avg(" + PROGRESS_TABLE + ".MARK)";

        return db.rawQuery(query, null);
    }

    public static Cursor getComparByGroups(SQLiteDatabase db, String startDate, String endDate) {

        String query = "select "
                            + GROUP_TABLE + ".NAME, "
                            + GROUP_TABLE + ".COURSE, "
                            + SUBJECT_TABLE + ".SUBJECT, "
                            + " avg(" + PROGRESS_TABLE + ".MARK)"
                        + " from " + PROGRESS_TABLE
                            + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                            + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                            + " join " + SUBJECT_TABLE + " on " + PROGRESS_TABLE + ".IDSUBJECT = " + SUBJECT_TABLE + ".IDSUBJECT"
                        + " where EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                        + " group by " + GROUP_TABLE + ".NAME, " + GROUP_TABLE + ".COURSE, " + SUBJECT_TABLE +".IDSUBJECT"
                        + " order by avg(" + PROGRESS_TABLE + ".MARK) desc";

        return db.rawQuery(query, null);
    }

    public static Cursor getAvgMarkByGroup(SQLiteDatabase db, String startDate, String endDate) {

        String query = "select " + GROUP_TABLE + ".NAME, " + GROUP_TABLE + ".COURSE, avg(" + PROGRESS_TABLE + ".MARK)"
                        + " from " + PROGRESS_TABLE
                            + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                            + " join " + GROUP_TABLE + " on " + GROUP_TABLE + ".IDGROUP = " + STUDENT_TABLE + ".IDGROUP"
                        + " where EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                        + " group by " + GROUP_TABLE + ".IDGROUP"
                        + " order by avg(" + PROGRESS_TABLE + ".MARK) desc";

        return db.rawQuery(query, null);
    }

    public static Cursor getAvgMarkByStudents(SQLiteDatabase db, int groupId, String startDate, String endDate) {

        String query = "select " + STUDENT_TABLE + ".NAME, avg(" + PROGRESS_TABLE + ".MARK)"
                        + " from " + PROGRESS_TABLE
                            + " join " + STUDENT_TABLE + " on " + STUDENT_TABLE + ".IDSTUDENT = " + PROGRESS_TABLE + ".IDSTUDENT"
                        + " where EXAMDATE between date('" + startDate + "') and date('" + endDate + "')"
                            + " and " + STUDENT_TABLE + ".IDGROUP = " + groupId
                        + " group by " + STUDENT_TABLE + ".IDSTUDENT";

        return db.rawQuery(query, null);
    }
}
