<b>Disaster I/O provides civilians the safest evacuation routes, and connects rescuers to those who need help the most.</b>

<b>Background</b>

We built this project because we were interested in challenges with a large impact - there are tens of thousands of people affected and even killed in natural disasters every year across the world, and we thought it would be extremely fulfilling and worthwhile to address an issue with such a massive potential impact.

<b>What It Does</b>

Disaster I/O can be summarized as 'the Waze for natural disasters.' Our program implements NASA's Earth Observatory Natural Event Tracker API to populate a web-based map application with markers of natural disaster events. However - what makes Disaster I/O exciting is the fact that users can report their own data as well! If a civilian is in distress, they can add themselves as a marker to alert rescuers to their exact location via geolocation. Those affected by the disaster can also work together by reporting fires, flooded roads, demolished buildings, and so on by uploading images to our application (which are spam-filtered by Google's Cloud Vision AI for image recognition). After an image is validated, data points are added to the map for all users to see - and Disaster I/O will route the safest ways for civilians to evacuate, and the safest and most efficient routes for law enforcement and rescue teams.

<b>NASA Data</b>

As mentioned above, we used NASA's EONET API. We used this data because it was a perfect fit for the mapping API we wanted to implement - the Mapbox API. It also conveniently provided the complete range of data we needed for our project!

<b>Future Plans</b>

Potential future implementations are the most exciting part of this challenge! We would like to develop this program to provide our own API, so that governments or regional emergency services could implement our data into their own applications and programs. We would also love to help those in the poor countries without access to the internet by building a text messaging service which allows civilians to share images to populate the map. This would allow police dispatchers, for example, to access information from those in the thick of the crisis, even if the users themselves can't get online. We'd also use the text messaging service to allow law enforcement and governments to blast critical information to those in disaster areas - perhaps providing our safe escape routes, or asking users to text photos of damaged infrastructure. 

We were lucky enough to make some exciting contacts through Space Apps mentors and volunteers, and intend to call organizations that could potentially use an API we created to see how we could best solve their pain points and meet their needs.

<b>Built With?</b>

We built our program with HTML, CSS, JavaScript, jQuery, the jQuery-UI library, the Mapbox API, and the NASA EONET API. We would love to eventually implement a back-end to offer our own API for use, which we would most likely do using Java. 


<b>Updated Code</b>

So far we have added real-time updates as well as a new java backend to control the data in and out, we use a web hook to send data real-time allowing users send crowd updates and send sos updates.
