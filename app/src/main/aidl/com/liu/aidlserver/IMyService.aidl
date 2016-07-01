// IMyService.aidl
package com.liu.aidlserver;

// Declare any non-default types here with import statements
import com.liu.aidlserver.Student;
interface IMyService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    List<Student> getStudent();
    void addStudent(in Student student);
}
