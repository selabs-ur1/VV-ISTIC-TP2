# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset (see the [pmd install instruction](./pmd-help.md)). Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

## Answer

pmd check -f html -R rulesets/java/quickstart.xml -d C:\Users\Ethan\Documents\TPVV\projectExamples\commons-collections -r C:\Users\Ethan\Documents\TPVV\projectExamples\reports\report-common-collection.html

I runned PMD on the apache/commons-collections source code.

true positive :
"Avoid using implementation types like 'ArrayList'; use the interface instead"
Using the interface rather than the implementation is usually a good practice. This way, you don't depend on a specific implementation but on an interface, which increases reusability. You can work with multiple implementations that implement the same interface, making your code more flexible and easier to maintain or extend in the future.

false positive :
"This class has only private constructors and may be final"
While PMD suggests making classes with only private constructors final, this is not always necessary. Some classes may have private constructors to prevent instantiation but still may not need to be final. Making the class finals restrict its flexibility and future extensibility for no good upside as a trade off. 
