/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.za.mom.tests;

import com.google.ratel.core.RatelService;
import com.google.ratel.util.ClassFinder;
import java.util.Set;

/**
 *
 */
public class ClassFinderTest {

    public static void main(String args[]) {
             ClassFinder classFinder = new ClassFinder(null, RatelService.class, "co.za.mom");
             Set<Class> subTypes = classFinder.getClasses();

    
     System.out.println("annotated types: " + subTypes);

    }
}
