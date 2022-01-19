class Scheduler:
    
    def __init__(self):
        pass

    def loadData(self):
        pass 

    def setMaxPerSemester(self, val):
        self.setMaxPerSemester = val

    def setFilename(self, filename):
        self.filename = filename

    def setAutoComplete(self, val):
        self.auto_complete = val

    def showCurriculum(self):
        pass 

    def showSchedule(self):
        schedule = self.makeSchedule()
        self.showSchedule(schedule)

    def showSchedule(self, arr):
        pass 

    def isSatisfied(self, taken, prereqs):
        
        for course in prereqs:
            if (course not in taken):
                return False

        return True

    def getOptions(self, taken):
        
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
        pass 

    def makeSchedule(self):
        pass 

    def help(self):
        pass 


def main():
    print("Main")

if __name__ == "__main__":
    main()
