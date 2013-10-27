/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.za.mom.tests;

import com.google.ratel.core.RatelService;
import com.google.ratel.util.ClassPathScanner;
import java.util.Set;

/**
 *
 */
public class ClassFinderTest {

    public static void main(String args[]) {
             ClassPathScanner classFinder = new ClassPathScanner(null, RatelService.class, "co.za.mom");
             Set<Class> subTypes = classFinder.scan();

    
     System.out.println("annotated types: " + subTypes);

    }
}
