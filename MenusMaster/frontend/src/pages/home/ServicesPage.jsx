import React from 'react'

import MainLayout from '../../components/MainLayout'
import Hero from './container/Hero'
import CTA from './container/CTA'

const HomePage = () => {
  return (
    <MainLayout>
      <Hero />
      <CTA />
    </MainLayout>
  )
}

export default HomePage
