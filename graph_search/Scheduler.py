
class Scheduler:
    '''
    A class used to help a student schedule courses.

    Attributes:
    ----------
    curriculum : dict
        Dictionary representing the courses in the curriculum and their prereqs. Keys are the courses and the values is a list of prereqs.
    filename : str
        String representing the filename holding the curriculum.
    maxPerSemester : int
        Integer that determines how many courses the student can take each semester.
    auto_complete : boolean
        Boolean value. If true, the scheduler will pick courses, False to use user input.

    Methods:
    ----------
    loadData(filename = "comp_sci.txt", maxPerSemester = 3, auto_complete = False):
        Loads the data into curriculum from a given filename.
    setMaxPerSemester(val):
        Sets the maximum courses the student can take per semester.
    setFilename(filename):
        Sets the filename variable
    setAutoComplete(val):
        Sets the auto_complete variable
    showCurriculum():
        Prints the curriculum to the console
    showSchedule():
        Prints the schedule to the console
    showSchedule(arr):
        Overloaded showSchedule method that will take an arr and print it to the console
    isSatisfied(taken, prereqs):
        Compares courses that have been taken and prereqs for a course to see if the student can take the course in question
    getOptions(taken):
        Gets the options the student has for courses given the courses already taken
    printOptions(options):
        Prints the current semesters options to the console. Used to get user input.
    getUserInput(options):
        Gets user input so that the user can pick their own courses
    '''
    
    def __init__(self):
        '''Basic constructor method for the Scheduler class.'''
        # Dictionary to hold the curriculum
        self.curriculum = {}

    def loadData(self, filename = "comp_sci.txt", maxPerSemester = 3, auto_complete = False):
        '''Method that will load all of the data for the Scheduler given the parameters given.
        
        Parameters
        -----------
        filename : str 
            Filename with the curriculum data. (default = 'comp_sci.txt')
        maxPerSemester : int
            The maximum number of courses that can be taken each semester. (default = 3)
        auto_complete : boolean
            True if the Scheduler should pick courses, False if user wants to give input. (default = False)

        Raises:
        ----------
        FileNotFoundError
            If there is an issue with reading from the file provided, error is thrown.
         '''

        # Set all of the instance variables
        if maxPerSemester > 0:
            self.setMaxPerSemester = maxPerSemester

        self.filename = filename
        self.auto_complete = auto_complete

        # Load all of the data given the filename
        try:
            lines = list()
            # Open file in read mode
            with open(self.filename, 'r') as f:
                # Read and parse lines and store the information in the curriculum variable
                lines = f.readlines()

            # Parse line by line
            for line in lines:
                courses = line.split(" ")
                # Get current course and its prereqs
                course = courses[0].strip()
                prereqs = courses[1:]
                # Set the curriculum
                self.curriculum[course] = prereqs   

        except:
            raise FileNotFoundError("The file {0} does not exist".format(filename))



    def setMaxPerSemester(self, val):
        '''Sets the maxPerSemester variable'''
        self.setMaxPerSemester = val

    def setFilename(self, filename):
        '''Sets the filename variable'''
        self.filename = filename

    def setAutoComplete(self, val):
        '''Sets the auto_complete variable'''
        self.auto_complete = val

    def showCurriculum(self):
        '''Prints the curriculum to the console'''
        
        # Print the header
        print("\n----------------COMPUTER SCIENCE CURRICULUM--------------\n")

        count = 1

        for k, v in self.curriculum.items():
            line = str(count) + ") "
            line += k + ": \n     prerequisites => "
            line += v + "\n"

            print(line)
            count++

    def showSchedule(self):
        '''Prints the students schedule to the console'''
        schedule = self.makeSchedule()
        self.showSchedule(schedule)

    def showSchedule(self, arr):
        '''Prints the students schedule to the console.'''
        pass 

    def isSatisfied(self, taken, prereqs):
        ''''''
        
        for course in prereqs:
            if (course not in taken):
                return False

        return True

    def getOptions(self, taken):
        ''''''
        
        options = []

        for k, v in self.curriculum.items():

            if (k not in taken):
                if (self.isSatisfied(taken, v)):
                    options.append(k)

        return options

    def printOptions(self, options):
        pass 

    def getUserInput(self, options):
        pass 

    def pickCourses(self, options):
        ''''''
        # Holds the final picked courses for one semester
        picked = list()

        if(self.auto_complete): # Student has opted for automatic scheduling
            # Less options available than the maximum per semester allowed
            if(len(options) <= self.maxPerSemester):
                picked = options # All options are chosen for the current semester
            else:
                picked = options[0:self.setMaxPerSemester]

        else: # Manual selection
            
            self.printOptions(options)
            picked = self.getUserInput(options)

        # Return the current semester
        return picked

    def makeSchedule(self):
        ''''''

        # Sets all the lists that are used to make final schedule
        final_schedule = list()
        taken = list()
        options = self.getOptions(taken)

        # Go until the user is out of options
        while(len(options) == 0):
            current_semester = self.pickCourses(options)

            if(len(current_semester) == 0):

                final_schedule.append(current_semester)
                taken.append(current_semester)

            # Update the options for the next semester
            options = self.getOptions(taken)

        return final_schedule # Return the final schedule of student

    def help(self):
        '''Help method'''
        pass 

if __name__ == "__main__":

    # Default values to make scheduler
    auto_complete = False
    maxPerSemester = 5
    filename = "comp_sci.txt"

    # Initialize the schedule given the values
    scheduler = Scheduler()
    scheduler.loadData(filename = filename, 
                        maxPerSemester = maxPerSemester, 
                        auto_complete = auto_complete)

    # Make and display the schedule
    schedule = scheduler.makeSchedule()
    scheduler.showSchedule(schedule) 
