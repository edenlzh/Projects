import React from 'react'
import MainLayout from '../components/MainLayout'
import CTA from './home/container/CTA'

const About = () => {
  return (
    <MainLayout>
        <section className='container mx-auto max-w-5xl flex flex-col px-5 py-5 lg:flex-row lg:gap-x-5 lg:items-start'>
          <article className='flex-1'>
          <h1 className='font-roboto text-3xl text-center font-bold mb-12 text-dark-soft md:text-5xl lg:text-4xl xl:text-5xl lg:text-left lg:max-w-[540px]'>About us</h1>
            <div className='mt-4 text-dark-soft'>
              <p className='leading-7'>
              Welcome to MenuMaster! We are your AI chef assistant providing you with the most exciting vegetarian recipes and cooking tips. Our website is designed to provide high-quality recipes and dish tutorials to vegetarians and people interested in healthy eating around the world.
              </p><br />
              <h2 className='leading-7 font-bold'>Discover delicious food and learn how to make it</h2><br />
              <p className='leading-7'>
              By talking to our AI assistant, you can easily discover your favorite vegetarian dishes and learn how to make them. No more worrying about what to eat, MenuMaster will provide you with inspiration and guidance.
              </p><br />
              <h2 className='leading-7 font-bold'>Personalized experience</h2><br />
              <p className='leading-7'>
              MenuMaster is committed to providing a flexible, easy-to-use and personalized learning and cooking experience. Our AI assistant will recommend recipes and food options that are best for you based on your tastes and preferences.
              </p><br />
              <h2 className='leading-7 font-bold'>Community interaction</h2><br />
              <p className='leading-7'>
              In addition to learning to cook, you can also share your cooking experiences and food insights with other users in our community discussion forum. Exchange experiences and explore the delicious world of vegetarian food together!
              </p><br />
              <h2 className='leading-7 font-bold'>Technological innovation</h2><br />
              <p className='leading-7'>
              MenuMaster's technical architecture uses advanced technologies, including the JavaScript programming language, MongoDB database, React.js and Tailwind CSS front-end builds. Even more exciting is that we have also integrated ChatGPT API to provide you with an intelligent AI assistant to help you answer questions, discover new recipes and learn cooking skills.
              </p><br />
              <p className='leading-7'>
              Whether you are a vegetarian, someone who wants to eat healthy, or just a regular person who wants to eat some vegetables, MenuMaster will bring you a joyful cooking experience and endless food exploration! Come join us and discover the delicious world of vegetarian food!
              </p><br />
            </div>
          </article>
        </section>
        <CTA />
    </MainLayout>
  )
}

export default About