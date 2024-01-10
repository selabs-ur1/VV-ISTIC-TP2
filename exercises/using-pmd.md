# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

In the File `gazelle-tm/gazelle-tm-ejb/src/main/java/net/ihe/gazelle/IO/TestImporterJob.java` of gazelle test management
application, pmd found usage of `new Long(...)`, which is not-encouraged: 

```
QuartzDispatcher.instance().scheduleTimedEvent("importTest", new TimerSchedule(new Long(2000)), tests, nbTests, counter, deleteImportedFile, xmlFile, initUser);
```
 
We can replace it with this to fix the error: 

```
QuartzDispatcher.instance().scheduleTimedEvent("importTest", new TimerSchedule(Long.valueOf(2000)), tests, nbTests, counter, deleteImportedFile, xmlFile, initUser);
```
 
The issue is that `new Long(...)` will always create a new Object in memory, while `Long.valueOf(...)` will either
create a new Object or return a reference to a previously cached Long of the same value, saving on memory.

In the file `gazelle-tm/gazelle-tm-ejb/src/main/java/net/ihe/gazelle/tm/application/gui/HomeBeanGui.java`, pmd found an 
unused method. But this method is used in a JSP file which pmd won't analyse.
