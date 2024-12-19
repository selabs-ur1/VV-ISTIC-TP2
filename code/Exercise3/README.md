# Code of your exercise

Test de ma regle : 
```
public class NestedIfTest {
    public void testNestedIf(int a, int b, int c) {
        if (a > 0) {
            if (b > 0) {
                if (c > 0) {
                    System.out.println("All values are positive!");
                }
            }
        }
    }
}
```
Règle :
```
<rule name="IfRule"
      language="java"
      message="Number of imbriquation of if more than 3"
      class="net.sourceforge.pmd.lang.rule.xpath.XPathRule">
   <description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="xpath">
         <value>
<![CDATA[
//IfStatement[descendant::Block/IfStatement[descendant::Block/IfStatement]]
]]>
         </value>
      </property>
   </properties>
</rule>
```