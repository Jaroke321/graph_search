
class Scheduler:
    ''''''
    
    def __init__(self):
        ''''''
        # Dictionary to hold the curriculum
        self.curriculum = {}

    def loadData(self, filename = None, maxPerSemester = 3, auto_complete = True):
        ''''''

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
            print("[!] ERROR: Something went wrong while reading from the file {0}"
                .format(self.filename))



    def setMaxPerSemester(self, val):
        ''''''
        self.setMaxPerSemester = val

    def setFilename(self, filename):
        ''''''
        self.filename = filename

    def setAutoComplete(self, val):
        ''''''
        self.auto_complete = val

    def showCurriculum(self):
        ''''''
        pass 

    def showSchedule(self):
        ''''''
        schedule = self.makeSchedule()
        self.showSchedule(schedule)

    def showSchedule(self, arr):
        ''''''
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
    scheduler.showSchedule() 
